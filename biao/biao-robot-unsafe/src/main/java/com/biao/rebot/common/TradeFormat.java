package com.biao.rebot.common;

import com.biao.rebot.config.RobotWeight;

import java.math.BigDecimal;

/**
 * TradeFormat.
 * <p>
 * 价格格式化.
 * <p>
 * 18-12-27下午12:53
 *
 *  "" sixh
 */
public class TradeFormat {

    /**
     * Price format big decimal.
     *
     * @param weight the weight
     * @param price  the price
     * @return the big decimal
     */
    public static BigDecimal priceFormat(RobotWeight weight, BigDecimal price){
    return price.setScale(weight.getSymbolInfo().getPriceScale(),BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Volume format big decimal.
     *
     * @param weight the weight
     * @param volume  the volme
     * @return the big decimal
     */
    public static BigDecimal volumeFormat(RobotWeight weight, BigDecimal volume){
        return volume.setScale(weight.getSymbolInfo().getVolumeScale(),BigDecimal.ROUND_HALF_UP);
    }
}
