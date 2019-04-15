package com.biao.util;

import com.biao.constant.TradeConstant;

import java.math.BigDecimal;

/**
 *  ""
 */
public class TradeCompute {
    /**
     * 加法；
     *
     * @param left  左边
     * @param right 右边
     * @return 计算后的结果；
     */
    public static BigDecimal add(BigDecimal left, BigDecimal right) {
        return left.add(right);
    }

    /**
     * 减法；
     *
     * @param left  左边
     * @param right 右边
     * @return 计算后的结果；
     */
    public static BigDecimal subtract(BigDecimal left, BigDecimal right) {
        return left.subtract(right);

    }

    /**
     * 乘法
     *
     * @param left  左边
     * @param right 右边
     * @return 计算后的结果；
     */
    public static BigDecimal multiply(BigDecimal left, BigDecimal right) {
        return left.multiply(right);

    }

    /**
     * 乘法
     *
     * @param left  左边
     * @param right 右边
     * @return 计算后的结果；
     */
    public static BigDecimal multiply(BigDecimal left, BigDecimal right, int scale) {
        return left.multiply(right).setScale(scale, BigDecimal.ROUND_HALF_UP);

    }

    /**
     * 除法；
     *
     * @param left  左边
     * @param right 右边
     * @return 计算后的结果；
     */
    public static BigDecimal divide(BigDecimal left, BigDecimal right) {
        return divide(left, right, 16);
    }

    /**
     * 除法；
     *
     * @param left  左边
     * @param right 右边
     * @return 计算后的结果；
     */
    public static BigDecimal divide(BigDecimal left, BigDecimal right, int scale) {
        if (right.doubleValue() == 0) {
            throw new RuntimeException("right is 0");
        }
        left = left.multiply(TradeConstant.TRADE_LENGTH);
        right = right.multiply(TradeConstant.TRADE_LENGTH);
        return left.divide(right, scale, BigDecimal.ROUND_HALF_UP);
    }
}
