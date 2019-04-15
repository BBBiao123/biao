package com.biao.previous;

import com.biao.constant.TradeConstant;
import com.biao.enums.TradeEnum;
import com.biao.pojo.TradeDto;
import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.Set;

/**
 * The type Tools.
 *
 */
public class Tools {
    /**
     * 生成一个换存KEY;
     *
     * @param dto dto;
     * @return key ;
     */
    public static String asCacheKey(TradeDto dto) {
        return asCacheKey(dto.getType(), dto.getCoinMain(), dto.getCoinOther());
    }

    /**
     * 生成一个缓存key;
     *
     * @param type      类型;
     * @param coinMain  主币
     * @param coinOther 交易币;
     * @return key ;
     */
    public static String asCacheKey(TradeEnum type, String coinMain, String coinOther) {
        return Joiner.on("_").join(type.redisKey(coinMain, coinOther), type.name());
    }

    /**
     * 获取一个买的第一条数据;
     *
     * @param coinMain  主币
     * @param coinOther 交易币;
     * @param template  redis;
     * @return 表达式 ;
     */
    @SuppressWarnings("all")
    public static Supplier<String> getBuyFirst(String coinMain, String coinOther, RedisTemplate template) {
        return () -> {
            long size = template.opsForZSet().size(TradeEnum.BUY.redisKey(coinMain,
                    coinOther));
            if (size == 0) {
                return null;
            }
            Set set = template.opsForZSet().range(TradeEnum.BUY
                            .redisKey(coinMain
                                    , coinOther),
                    size - 1, size);
            //这里还是需要判断一下；
            if (CollectionUtils.isEmpty(set)) {
                return null;
            }
            return (String) set.stream().findFirst().get();
        };
    }

    /**
     * 获取一个卖的第一条数据;
     *
     * @param coinMain  主币
     * @param coinOther 交易币;
     * @param template  redis;
     * @return 表达式 ;
     */
    @SuppressWarnings("all")
    public static Supplier<String> getSellFirst(String coinMain, String coinOther, RedisTemplate template) {
        return () -> {
            Set set = template.opsForZSet().range(TradeEnum.SELL
                            .redisKey(coinMain
                                    , coinOther),
                    0,
                    1);
            if (CollectionUtils.isEmpty(set)) {
                return null;
            }
            return (String) set.stream().findFirst().get();
        };
    }

    /**
     * Gets pre.
     *
     * @param orderNo  the order no
     * @param template the template
     * @return the pre
     */
    @SuppressWarnings("all")
    public static Supplier<TradeDto> getPre(String orderNo, RedisTemplate template) {
        return ()-> {
            Optional<Object> o = Optional.ofNullable(template.opsForHash()
                    .get(TradeConstant.TRADE_PREPOSITION_KEY, orderNo));
            return (TradeDto) o.orElse(null);
        };
    }
}
