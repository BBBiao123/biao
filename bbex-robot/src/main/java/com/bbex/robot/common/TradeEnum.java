package com.bbex.robot.common;


/**
 * @author p
 * @date 2018/4/7
 * 交易类型 ；
 */
public enum TradeEnum {

    /**
     * 买入
     */
    BUY,
    /**
     * 卖出
     */
    SELL;


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

