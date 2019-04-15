package com.biao.init;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.biao.binance.config.ExPair;
import com.biao.binance.config.KlineData;
import com.biao.dao.ExPairDao;
import com.biao.dao.KlineDataDao;
import com.biao.enums.KlineTimeEnum;
import com.biao.kline.KlineHanderService;
import com.biao.kline.cache.GenericKlineCacheData;
import com.biao.kline.cache.KlineCacheDataList;
import com.biao.kline.disruptor.DisruptorManager;
import com.biao.kline.vo.KlineDisruptorVO;
import com.biao.vo.KlineVO;

/**
 * The type Kline start init.
 */
@Component
public class KlineStartInit implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(KlineStartInit.class);

    @Autowired(required = false)
    private ExPairDao exPairDao;

    @Autowired(required = false)
    private KlineDataDao klineDataDao;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(8,
            new BasicThreadFactory.Builder().namingPattern("kline-task-%d").daemon(true).build());

    private static final int DEF_INTEL = 1;

    protected static final boolean startDisruptor = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            logger.info("spring 启动  初始化 内存数据  , redis -----> memory");
            List<ExPair> exPairs = exPairDao.findByList();
            if (!CollectionUtils.isEmpty(exPairs)) {
            	if (startDisruptor) {
                    DisruptorManager.instance().runConfig();
                }
                exPairs.stream().forEach(exPair -> {
                    for (KlineTimeEnum klineTime : KlineTimeEnum.values()) {
                        logger.info("初始化kline数据,coinMain:{},coinOther:{},klinaTimeEnum:{}", exPair.getPairOne(),
                                exPair.getPairOther(), klineTime.getMsg());
                        final String klineKeyStr = GenericKlineCacheData.buildKlineCacheKey(exPair.getPairOne(),
                                exPair.getPairOther(), klineTime.getMsg());
                        Map<Object, Object> redisMapDatas = redisTemplate.boundHashOps(klineKeyStr).entries();
                        if (!CollectionUtils.isEmpty(redisMapDatas)) {
                            logger.info("初始化kline数据,coinMain:{},coinOther:{},klinaTimeEnum:{},获取redis数据大小size:{}",
                                    exPair.getPairOne(), exPair.getPairOther(), klineTime.getMsg(), redisMapDatas.size());
                            Set<Object> keys = redisMapDatas.keySet();
                            List<Object> keyList = new ArrayList<>(keys);
                            List<LocalDateTime> filterKeyList = keyList.stream()
                                    .filter(key -> key != null && StringUtils.isNotBlank(key.toString())).map(key -> {
                                        try {
                                            return KlineHanderService.formatRedisCacheMapKey(key.toString());
                                        } catch (ParseException e) {
                                        }
                                        return null;
                                    }).filter(key -> key != null).collect(Collectors.toList());
                            filterKeyList.sort(new Comparator<LocalDateTime>() {
                                @Override
                                public int compare(LocalDateTime o1, LocalDateTime o2) {
                                    return (o1.isAfter(o2) ? -1 : 1);
                                }
                            });
                            List<LocalDateTime> reverseList = null;
                            int subLength = GenericKlineCacheData.DEF_CAP;
                            if (filterKeyList.size() > subLength) {
                                reverseList = filterKeyList.subList(0, subLength);
                            } else {
                                reverseList = filterKeyList;
                            }
                            logger.info("初始化kline数据,coinMain:{},coinOther:{},klinaTimeEnum:{},截取后的数据大小size:{}",
                                    exPair.getPairOne(), exPair.getPairOther(), klineTime.getMsg(), reverseList.size());
                            reverseList.sort((new Comparator<LocalDateTime>() {
                                @Override
                                public int compare(LocalDateTime o1, LocalDateTime o2) {
                                    return (o1.isAfter(o2) ? 1 : -1);
                                }
                            }));
                            reverseList.stream().forEach(key -> {
                                String keyStr = KlineHanderService.formatRedisCacheMapKey(key);
                                logger.info("获取redis数据klineKeyStr:{},归集时间点key:{}", klineKeyStr, keyStr);
                                KlineCacheDataList.initRedisToCache(exPair.getPairOne(), exPair.getPairOther(), klineTime,
                                        keyStr, (KlineVO) redisMapDatas.get(keyStr));
                            });
                        } else {
                            logger.warn("初始化kline数据,coinMain:{},coinOther:{},klinaTimeEnum:{},redis数据内存为空",
                                    exPair.getPairOne(), exPair.getPairOther(), klineTime.getMsg());
                        }
                    }
                });
            }else {
            	logger.warn("初始化kline数据到内存,查询交易对为空");
            }
            GenericKlineCacheData.initComplate() ;
        }
        // 增加任务 清理redis数据
        executorService.scheduleAtFixedRate(new ScheduledKlineTask(redisTemplate, exPairDao, klineDataDao), DEF_INTEL,
                DEF_INTEL, TimeUnit.DAYS);
    }

    private static class ScheduledKlineTask implements Runnable {

        private static Logger logger = LoggerFactory.getLogger(ScheduledKlineTask.class);

        private RedisTemplate<String, Object> redisTemplate;

        private ExPairDao exPairDao;

        private KlineDataDao klineDataDao;

        public ScheduledKlineTask(RedisTemplate<String, Object> redisTemplate, ExPairDao exPairDao,
                                  KlineDataDao klineDataDao) {
            this.redisTemplate = redisTemplate;
            this.exPairDao = exPairDao;
            this.klineDataDao = klineDataDao;
        }

        @Override
        public void run() {
            try {
                logger.info("=============kline task clear redis data to mysql  start ===========================");
                List<ExPair> exPairs = exPairDao.findByList();
                if (CollectionUtils.isEmpty(exPairs)) {
                    logger.warn("kline task 清理数据,查询交易对为空");
                    return;
                }
                LocalDateTime endTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
                exPairs.stream().forEach(exPair -> {
                    for (KlineTimeEnum klineTime : KlineTimeEnum.values()) {
                        logger.info("kline task 清理数据,coinMain:{},coinOther:{},klinaTimeEnum:{}", exPair.getPairOne(),
                                exPair.getPairOther(), klineTime.getMsg());
                        final String klineKeyStr = GenericKlineCacheData.buildKlineCacheKey(exPair.getPairOne(),
                                exPair.getPairOther(), klineTime.getMsg());
                        Map<Object, Object> redisMapDatas = redisTemplate.boundHashOps(klineKeyStr).entries();
                        if (!CollectionUtils.isEmpty(redisMapDatas)) {
                            logger.info("kline task 清理数据,coinMain:{},coinOther:{},klinaTimeEnum:{},获取redis数据大小size:{}",
                                    exPair.getPairOne(), exPair.getPairOther(), klineTime.getMsg(),
                                    redisMapDatas.size());
                            Set<Object> keys = redisMapDatas.keySet();
                            if (keys.size() < GenericKlineCacheData.DEF_CAP) {
                                logger.info("kline task 清理数据,coinMain:{},coinOther:{},klinaTimeEnum:{},数据没有超过容量,不需要清理",
                                        exPair.getPairOne(), exPair.getPairOther(), klineTime.getMsg());
                                return;
                            }
                            logger.info("kline task 清理数据,coinMain:{},coinOther:{},klinaTimeEnum:{},截止时间endTime:{}",
                                    exPair.getPairOne(), exPair.getPairOther(), klineTime.getMsg(), endTime);
                            // 过滤出需要清除的数据集合
                            List<LocalDateTime> clearRedisKeys = keys.stream()
                                    .filter(key -> key != null && StringUtils.isNotBlank(key.toString())).map(key -> {
                                        try {
                                            return KlineHanderService.formatRedisCacheMapKey(key.toString());
                                        } catch (ParseException e) {
                                        }
                                        return null;
                                    }).filter(key -> key != null).filter(key -> key.isBefore(endTime))
                                    .collect(Collectors.toList());
                            if (clearRedisKeys.size() > 0) {
                                Object[] deleteKeys = new String[clearRedisKeys.size()];
                                for (int i = 0; i < clearRedisKeys.size(); i++) {
                                    LocalDateTime key = clearRedisKeys.get(i);
                                    String keyStr = KlineHanderService.formatRedisCacheMapKey(key);
                                    logger.info("kline task 清理数据,klineKeyStr:{},归集时间点key:{}", klineKeyStr, keyStr);
                                    // 异步记录数据库
                                    if (KlineStartInit.startDisruptor) {
                                        KlineDisruptorVO disruptorVO = new KlineDisruptorVO();
                                        disruptorVO.setKlineTimeEnum(klineTime);
                                        disruptorVO.setKlineVo((KlineVO) redisMapDatas.get(keyStr));
                                        disruptorVO.setFormatTradeTime(key);
                                        disruptorVO.setExPair(exPair);
                                        DisruptorManager.publishDisruptor(disruptorVO, 1);
                                    } else {
                                        KlineData klineData = KlineData.createKlineData(klineTime.getMsg(), key,
                                                (KlineVO) redisMapDatas.get(keyStr),exPair);
                                        klineDataDao.insert(klineData);
                                        deleteKeys[i] = keyStr;
                                    }
                                }
                                Long delete = redisTemplate.boundHashOps(klineKeyStr).delete(deleteKeys);
                                logger.info("kline task 清理数据,klineKeyStr:{},redis map delete size:{}", klineKeyStr,
                                        delete);
                            }
                        } else {
                            logger.warn("kline task 清理数据,coinMain:{},coinOther:{},klinaTimeEnum:{},redis数据内存为空",
                                    exPair.getPairOne(), exPair.getPairOther(), klineTime.getMsg());
                        }
                    }
                });
                logger.info("=============kline task clear redis data to mysql  end   ===========================");
            } catch (Exception e) {
                logger.error("syn redis to mysql thread error:{}", e);
            }

        }
    }
}
