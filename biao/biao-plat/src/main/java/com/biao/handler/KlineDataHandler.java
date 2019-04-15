package com.biao.handler;

import com.biao.constant.RedisKeyConstant;
import com.biao.vo.KlineResult;
import com.biao.vo.KlineVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  ""(Myth)
 */
@Component
@SuppressWarnings("all")
public class KlineDataHandler {

    private final RedisTemplate redisTemplate;

    private static final int max = 2000;

    @Autowired
    public KlineDataHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public KlineResult buildResult(String coinOther, String coinMain, String time) {
        KlineResult klineResult = new KlineResult();
        klineResult.setType("kline");
        klineResult.setCode(0);
        final String key = RedisKeyConstant.buildTaskKlineStatKey(coinMain, coinOther, time);
        List<KlineVO> klineVOList = (List<KlineVO>) redisTemplate.opsForList().range(key, 0, max - 1);
        if (CollectionUtils.isNotEmpty(klineVOList)) {
            final List<KlineVO> rs =
                    klineVOList.stream().sorted(Comparator.comparing(e -> e.getT()))
                            .collect(Collectors.toList());
            klineResult.setData(rs);
        }
        return klineResult;
    }
}
