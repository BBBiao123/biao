package com.biao.cache;

import com.biao.constant.Constants;
import com.biao.constant.TradeConstant;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.enums.TradeEnum;
import com.biao.pojo.RemovePlatOrderDTO;
import com.biao.pojo.TradeDto;
import com.biao.redis.RedisCacheManager;
import com.biao.vo.PlatOrderVO;
import com.biao.vo.redis.RedisExPairVO;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *  ""(""611 @ qq.com)
 */
@Component
@SuppressWarnings("all")
public class PlatOrderCacheManager implements CommandLineRunner {

    private static final Map<String, List<PlatOrderVO>> buyAllMap = Maps.newConcurrentMap();

    private static final Map<String, List<PlatOrderVO>> sellAllMap = Maps.newConcurrentMap();

    private final ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(1,
                    BbexThreadFactory.create("plat-order-task", false));

    private final RedisTemplate redisTemplate;

    private final RedisCacheManager redisCacheManager;

    public List<PlatOrderVO> cacheBuyOrder(String key) {
        return buyAllMap.get(key);
    }

    public List<PlatOrderVO> cacheSellOrder(String key) {
        return sellAllMap.get(key);
    }

    @Autowired
    public PlatOrderCacheManager(RedisTemplate redisTemplate, RedisCacheManager redisCacheManager) {
        this.redisTemplate = redisTemplate;
        this.redisCacheManager = redisCacheManager;
    }

    public void removeCache(RemovePlatOrderDTO tradeDto) {
        if (Objects.isNull(tradeDto)) {
            return;
        }
        final TradeEnum tradeEnum = tradeDto.getType();
        String key = buildMapKey(tradeDto.getCoinMain(), tradeDto.getCoinOther());
        if (Objects.equals(tradeEnum.name(), TradeEnum.BUY.name())) {
            removeMap(key, tradeDto, buyAllMap);
        } else {
            removeMap(key, tradeDto, sellAllMap);
        }
    }

    public void addCache(TradeDto tradeDto) {
        if (Objects.isNull(tradeDto)) {
            return;
        }
        final TradeEnum tradeEnum = tradeDto.getType();
        String key = buildMapKey(tradeDto.getCoinMain(), tradeDto.getCoinOther());
        if (Objects.equals(tradeEnum.name(), TradeEnum.BUY.name())) {
            cacheMap(key, tradeDto, buyAllMap);
        } else {
            cacheMap(key, tradeDto, sellAllMap);
        }
    }

    @Override
    public void run(String... args) {
        refreshMap();
        scheduledExecutorService
                .scheduleWithFixedDelay(() -> {
                    try {
                        refreshMap();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 60, 120, TimeUnit.SECONDS);
    }

    private void refreshMap() {
        List<RedisExPairVO> allExpair = redisCacheManager.acquireAllExpair();
        for (RedisExPairVO exPairVO : allExpair) {
            String buyRedisKey = tradeRedisKey(TradeEnum.BUY, exPairVO.getPairOne(), exPairVO.getPairOther());
            final List<PlatOrderVO> buyList = buildPlatOrderVO(buyRedisKey);
            buyAllMap.put(buildMapKey(exPairVO.getPairOne(), exPairVO.getPairOther()), buyList);

            String sellRedisKey = tradeRedisKey(TradeEnum.SELL, exPairVO.getPairOne(), exPairVO.getPairOther());
            final List<PlatOrderVO> sellList = buildPlatOrderVO(sellRedisKey);

            sellAllMap.put(buildMapKey(exPairVO.getPairOne(), exPairVO.getPairOther()), sellList);

        }
    }

    private void removeMap(String key, RemovePlatOrderDTO tradeDto, Map<String, List<PlatOrderVO>> map) {
        final List<PlatOrderVO> platOrderVOS = map.get(key);
        if (CollectionUtils.isNotEmpty(platOrderVOS)) {
            platOrderVOS.removeIf(o -> o.getOrderNo().equals(tradeDto.getOrderNo()));
        }
    }

    private void cacheMap(String key, TradeDto tradeDto, Map<String, List<PlatOrderVO>> map) {
        if (map.containsKey(key)) {
            final List<PlatOrderVO> platOrderVOList = map.get(key);
            if (StringUtils.isNoneBlank(tradeDto.getCancelLock())) {
                final List<PlatOrderVO> resultList = platOrderVOList.stream()
                        .filter(r -> !r.getOrderNo()
                                .equals(tradeDto.getOrderNo()))
                        .collect(Collectors.toCollection(() -> Collections.synchronizedList(new LinkedList<>())));
                resultList.add(PlatOrderVO.transform(tradeDto));
                map.put(key, resultList);
            } else {
                platOrderVOList.add(PlatOrderVO.transform(tradeDto));
                map.put(key, platOrderVOList);
            }
        } else {
            final List<PlatOrderVO> lists = Collections.synchronizedList(new LinkedList<>());
            lists.add(PlatOrderVO.transform(tradeDto));
            map.put(key, lists);
        }
    }

    private List<PlatOrderVO> buildPlatOrderVO(final String redisKey) {
        final Set<String> sets = redisTemplate.opsForZSet().range(redisKey, 0, -1);
        if (CollectionUtils.isEmpty(sets)) {
            return Collections.synchronizedList(new LinkedList<>());
        }

        final List<TradeDto> tradeDtoList = redisTemplate.opsForHash()
                .multiGet(TradeConstant.TRADE_PREPOSITION_KEY, sets);
        if (CollectionUtils.isNotEmpty(tradeDtoList)) {
            return tradeDtoList.stream()
                    .filter(Objects::nonNull)
                    .map(PlatOrderVO::transform)
                    .collect(Collectors.toCollection(() -> Collections.synchronizedList(new LinkedList<>())));
        }
        return Collections.synchronizedList(new LinkedList<>());
    }

    private String tradeRedisKey(final TradeEnum tradeEnum, final String coinMain, final String coinOther) {
        return tradeEnum.redisKey(coinMain, coinOther);
    }

    private String buildMapKey(final String coinMain, final String coinOther) {
        return String.join(Constants.JOIN, coinOther, coinMain);
    }

}
