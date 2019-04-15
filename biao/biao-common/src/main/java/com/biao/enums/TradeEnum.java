package com.biao.enums;

import com.biao.constant.TradeConstant;

/**
 *
 *
 * @date 2018/4/7
 * 交易类型 ；
 */
public enum TradeEnum {

    /**
     * 买入
     */
    BUY {
        @Override
        public String redisKey(String coinMainNo, String coinOther) {
            return TradeConstant.TRADE_REDIS_BUY_SET + ":" + coinMainNo + ":" + coinOther;
        }
    },
    /**
     * 卖出
     */
    SELL {
        @Override
        public String redisKey(String coinMainNo, String coinOther) {
            return TradeConstant.TRADE_REDIS_SELL_SET + ":" + coinMainNo + ":" + coinOther;
        }
    };

    /**
     * 返回一个相对应的RedisKey;
     *
     * @return key
     */
    public abstract String redisKey(String coinManNo, String coinOther);

    /**
     * 返回相反的Redis的Key
     *
     * @return key;
     */
    public String reverseRedisKey(String coinManNo, String coinOther) {
        switch (this) {
            case BUY:
                return SELL.redisKey(coinManNo, coinOther);
            case SELL:
                return BUY.redisKey(coinManNo, coinOther);
            default:
                return "";
        }
    }

    /**
     * 根据下标转换成当前对象；
     *
     * @param ordinal 下标；
     * @return this;
     */
    public static TradeEnum valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return TradeEnum.BUY;
            case 1:
                return TradeEnum.SELL;
            default:
                return TradeEnum.BUY;
        }
    }
}
