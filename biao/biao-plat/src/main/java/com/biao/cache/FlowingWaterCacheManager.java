package com.biao.cache;

import com.biao.pojo.MatchStreamDto;
import com.biao.reactive.data.mongo.domain.MatchStream;
import com.biao.reactive.data.mongo.service.MatchStreamService;
import com.biao.redis.RedisCacheManager;
import com.biao.vo.MatchStreamVO;
import com.biao.vo.redis.RedisExPairVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The type Flowing water cache manager.
 *
 *  ""(""611 @ qq.com)
 */
@Component
@SuppressWarnings("unchecked")
public class FlowingWaterCacheManager implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowingWaterCacheManager.class);

    private static final int TOP = 30;

    private final RedisCacheManager redisCacheManager;

    private final MatchStreamService matchStreamService;

    private Map<String, LinkedList<MatchStreamVO>> map = new ConcurrentHashMap<>();

    @Autowired
    public FlowingWaterCacheManager(final RedisCacheManager redisCacheManager,
                                    final MatchStreamService matchStreamService) {
        this.redisCacheManager = redisCacheManager;
        this.matchStreamService = matchStreamService;
    }

    /**
     * Find list.
     *
     * @param coinMain  the coin main
     * @param coinOther the coin other
     * @return the list
     */
    public List<MatchStreamVO> find(final String coinMain, final String coinOther) {
        return map.get(buildKey(coinMain, coinOther));
    }

    /**
     * Refresh.
     *
     * @param matchStreamDto the match stream dto
     */
    public synchronized void refresh(final MatchStreamDto matchStreamDto) {
        String key = buildKey(matchStreamDto.getCoinMain(), matchStreamDto.getCoinOther());
        final LinkedList<MatchStreamVO> matchStreamVOS =
                map.get(key);
        if (CollectionUtils.isNotEmpty(matchStreamVOS)) {
            final MatchStreamVO vo = MatchStreamVO.transform(matchStreamDto);
            matchStreamVOS.addFirst(vo);
            if (matchStreamVOS.size() > TOP) {
                final LinkedList<MatchStreamVO> collect =
                        matchStreamVOS.stream().limit(TOP)
                                .collect(Collectors.toCollection(LinkedList::new));
                map.put(key, collect);
            } else {
                map.put(key, matchStreamVOS);
        }
        } else {
            LinkedList<MatchStreamVO> vos = new LinkedList<>();
            vos.add(MatchStreamVO.transform(matchStreamDto));
            map.put(key, vos);
        }

    }


    @Override
    public void run(final String... args) {
        List<RedisExPairVO> allExpair = redisCacheManager.acquireAllExpair();
        for (RedisExPairVO exPairVO : allExpair) {
            final List<MatchStream> list = matchStreamService
                    .findTopByCoinMainAndCoinOther(exPairVO.getPairOne(),
                            exPairVO.getPairOther(), TOP);
            if (CollectionUtils.isNotEmpty(list)) {
                final LinkedList<MatchStreamVO> streamVOS = new LinkedList<>();
                for (MatchStream matchStream : list) {
                    if (matchStream != null) {
                        MatchStreamVO vo = MatchStreamVO.transfrom(matchStream);
                        streamVOS.add(vo);
                    }
                }
                map.put(buildKey(exPairVO.getPairOne(),
                        exPairVO.getPairOther()), streamVOS);
            }
        }
        LOGGER.info("初始化流水到缓存成功...服务开始启动...........");
    }

    private String buildKey(final String coinMain, final String coinOther) {
        return coinMain + "_" + coinOther;
    }

    /**
     * 刷新缓存
     */
    public void refreshMap(){
        List<RedisExPairVO> allExpair = redisCacheManager.acquireAllExpair();
        for (RedisExPairVO exPairVO : allExpair) {
            final List<MatchStream> list = matchStreamService
                    .findTopByCoinMainAndCoinOther(exPairVO.getPairOne(),
                            exPairVO.getPairOther(), TOP);
            if (CollectionUtils.isNotEmpty(list)) {
                final LinkedList<MatchStreamVO> streamVOS = new LinkedList<>();
                for (MatchStream matchStream : list) {
                    if (matchStream != null) {
                        MatchStreamVO vo = MatchStreamVO.transfrom(matchStream);
                        streamVOS.add(vo);
                    }
                }
                map.clear();
                map.put(buildKey(exPairVO.getPairOne(),
                        exPairVO.getPairOther()), streamVOS);
            }
        }
        LOGGER.info("刷新流水到缓存成功..............");
    }
}
