package com.biao.service.kline;

import com.biao.constant.RedisKeyConstant;
import com.biao.entity.kline.KlinePullConfig;
import com.biao.enums.KlineTimeEnum;
import com.biao.mapper.kline.KlinePullConfigDao;
import com.biao.reactive.data.mongo.domain.kline.KlineLog;
import com.biao.reactive.data.mongo.domain.kline.KlineStatLog;
import com.biao.reactive.data.mongo.service.KlineLogService;
import com.biao.reactive.data.mongo.service.KlineStatLogService;
import com.biao.service.KlineDataService;
import com.biao.util.DateUtils;
import com.biao.util.http.OkHttpTools;
import com.biao.vo.KlineVO;
import com.beust.jcommander.internal.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/**
 *  ""(Myth)
 */
@Service("klineDataService")
public class KlineDataServiceImpl implements KlineDataService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KlineDataServiceImpl.class);

    private static final Gson GOSN = new Gson();

    private static final String BINANCE_KLINE = "BINANCE:KLINE:PULLDATE:";

    private final KlinePullConfigDao klinePullConfigDao;

    private final RedisTemplate redisTemplate;

    private final KlineLogService klineLogService;

    private final KlineStatLogService klineStatLogService;

    private String[] intervals = new String[]{KlineTimeEnum.ONE_HOUR.getMsg(),
            KlineTimeEnum.TWO_HOUR.getMsg(), KlineTimeEnum.FOUR_HOUR.getMsg(),
            KlineTimeEnum.SIX_HOUR.getMsg(), KlineTimeEnum.TWELVE_HOUR.getMsg(),
            KlineTimeEnum.ONE_DAY.getMsg(),
            KlineTimeEnum.ONE_WEEK.getMsg(),
            KlineTimeEnum.ONE_MONTH.getMsg()};


    @Autowired(required = false)
    public KlineDataServiceImpl(KlinePullConfigDao klinePullConfigDao,
                                RedisTemplate redisTemplate,
                                KlineLogService klineLogService, KlineStatLogService klineStatLogService) {
        this.klinePullConfigDao = klinePullConfigDao;
        this.redisTemplate = redisTemplate;
        this.klineLogService = klineLogService;
        this.klineStatLogService = klineStatLogService;
    }

    @Override
    public void executePullData() {
        final List<KlinePullConfig> klinePullConfigs = klinePullConfigDao.findAll();
        if (CollectionUtils.isNotEmpty(klinePullConfigs)) {
            klinePullConfigs.forEach(KlinePullDataHandler::submit);
        }
    }

    @Override
    public void executePullGroupData() {
        final List<KlinePullConfig> klinePullConfigs = klinePullConfigDao.findAll();
        if (CollectionUtils.isNotEmpty(klinePullConfigs)) {
            for (KlinePullConfig config : klinePullConfigs) {
                for (String interval : intervals) {
                    final List<List<Object>> resultList = pullData(config, "&interval=" + interval);
                    if (CollectionUtils.isNotEmpty(resultList)) {
                        for (List<Object> rs : resultList) {
                            handleGroupData(config, rs, interval);
                        }
                    }
                }
            }
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public void handlerPullData(KlinePullConfig config) throws ParseException {
        final String coinMain = config.getCoinMain();
        final String coinOther = config.getCoinOther();
        List<List<Object>> resultList = pullData(config, "&interval=1m");
        if (CollectionUtils.isNotEmpty(resultList)) {
            LocalDateTime lastExeTime = null;
            String lastExeTimeStr = (String) redisTemplate.opsForValue().get(buildKey(coinOther, coinMain));
            if (StringUtils.isNotEmpty(lastExeTimeStr)) {
                lastExeTime = DateUtils.parseLocalDateTime(lastExeTimeStr);
            }
            for (List<Object> objects : resultList) {
                final Long timestamp = ((Double) objects.get(0)).longValue();
                final LocalDateTime dateTime = LocalDateTime
                        .ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
                if (Objects.isNull(lastExeTime)) {
                    saveMinDataToMongoDb(coinMain, coinOther, dateTime, objects);
                } else {
                    if (dateTime.isAfter(lastExeTime)) {
                        saveMinDataToMongoDb(coinMain, coinOther, dateTime, objects);
                    }
                }
            }
            redisTemplate.opsForValue().set(buildKey(config.getCoinOther(), config.getCoinMain()),
                    DateUtils.formaterLocalDateTime(LocalDateTime.now()));
        }
    }

    private List<List<Object>> pullData(KlinePullConfig config, String interval) {
        final String coinMain = config.getCoinMain();
        final String coinOther = config.getCoinOther();
        String url = config.getPullUrl() + "?symbol=" + coinOther + coinMain + interval;
        //本地测试使用 proxyGet 服务端使用 get
        LOGGER.info("拉取变kline数据，url:{}", url);
        try {
            final String result;
            if (config.getProxyed()) {
                result = OkHttpTools.getInstance().proxyGet(url);
            } else {
                result = OkHttpTools.getInstance().get(url);
            }
            Type type = new TypeToken<List<List<Object>>>() {
            }.getType();
            if (StringUtils.isNoneBlank(result)) {
                return GOSN.fromJson(result, type);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("币安拉取失败:{}", url);
            return null;
        }

    }

    @SuppressWarnings("all")
    private void handleGroupData(KlinePullConfig config, List<Object> objects, String interval) {
        //数据存一份到redis ，再存一份到mongo

        final String coinMain = config.getCoinMain();
        final String coinOther = config.getCoinOther();
        final String key = RedisKeyConstant.buildTaskKlineStatKey(coinMain, coinOther, interval);

        final String high = objects.get(2).toString();
        final String low = objects.get(3).toString();
        final String volume = objects.get(5).toString();
        final String open = objects.get(1).toString();
        final String close = objects.get(4).toString();
        Long t = ((Double) objects.get(0)).longValue();
        final String time = t.toString();

        KlineVO klineVO = new KlineVO();
        //最高
        klineVO.setH(high);
        //最低
        klineVO.setL(low);
        //成交量
        klineVO.setV(volume);
        //开盘
        klineVO.setO(open);
        klineVO.setC(close);
        klineVO.setT(time);
        klineVO.setS("ok");
        redisTemplate.opsForList().leftPush(key, klineVO);

        KlineStatLog klineStatLog = new KlineStatLog();
        klineStatLog.setCoinMain(config.getCoinMain());
        klineStatLog.setCoinOther(config.getCoinOther());
        //最高
        klineStatLog.setH(high);
        //最低
        klineStatLog.setL(low);
        //成交量
        klineStatLog.setV(volume);
        //开盘
        klineStatLog.setO(open);
        klineStatLog.setC(close);
        klineStatLog.setT(time);

        final LocalDateTime tradeTime = LocalDateTime
                .ofEpochSecond(t / 1000, 0, ZoneOffset.ofHours(8));

        klineStatLog.setTradeTime(tradeTime);

        klineStatLog.setKlineTimeUnit(interval);

        klineStatLogService.insert(klineStatLog);

    }


    private void saveMinDataToMongoDb(String coinMain, String coinOther, LocalDateTime dateTime, List<Object> objects) {

        List<KlineLog> logs = Lists.newArrayList(4);

        final BigDecimal divide = new BigDecimal(objects.get(5).toString())
                .divide(new BigDecimal(4), 2, RoundingMode.HALF_UP);

        KlineLog open = new KlineLog();
        open.setCoinMain(coinMain);
        open.setCoinOther(coinOther);
        open.setMinuteTime(dateTime);
        open.setTradeTime(dateTime.plusSeconds(1));
        open.setPrice(new BigDecimal(objects.get(1).toString()));
        open.setVolume(divide);

        logs.add(open);

        KlineLog high = new KlineLog();
        high.setCoinMain(coinMain);
        high.setCoinOther(coinOther);
        high.setMinuteTime(dateTime);
        high.setTradeTime(dateTime.plusSeconds(5));
        high.setPrice(new BigDecimal(objects.get(2).toString()));
        high.setVolume(divide);

        logs.add(high);

        KlineLog low = new KlineLog();
        low.setCoinMain(coinMain);
        low.setCoinOther(coinOther);
        low.setMinuteTime(dateTime);
        low.setTradeTime(dateTime.plusSeconds(5));
        low.setPrice(new BigDecimal(objects.get(3).toString()));
        low.setVolume(divide);

        logs.add(low);

        KlineLog receive = new KlineLog();
        receive.setCoinMain(coinMain);
        receive.setCoinOther(coinOther);
        receive.setMinuteTime(dateTime);
        receive.setTradeTime(dateTime.plusSeconds(50));
        receive.setPrice(new BigDecimal(objects.get(4).toString()));
        receive.setVolume(divide);

        logs.add(receive);

        klineLogService.batchInsert(logs);

    }

    private String buildKey(String coinOther, String coinMain) {
        return String.join("", BINANCE_KLINE, coinOther, coinMain);
    }


}
