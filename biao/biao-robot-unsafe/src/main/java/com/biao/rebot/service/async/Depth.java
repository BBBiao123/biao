package com.biao.rebot.service.async;

import lombok.Setter;

import java.math.BigDecimal;

/**
 * Depth.
 * <p>
 * 深度数据.
 * <p>
 * 18-12-18下午2:39
 *
 *  "" sixh
 */
@Setter
public class Depth implements AsyncDepth<Depth> {

    private String symbol;
    /**
     * 价格.
     */
    private BigDecimal price;
    /**
     * 数据.
     */
    private BigDecimal volume;

    @Override
    public Depth getAsks() {
        return this;
    }

    @Override
    public Depth getBids() {
        return this;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public BigDecimal getVolume() {
        return volume;
    }


}
