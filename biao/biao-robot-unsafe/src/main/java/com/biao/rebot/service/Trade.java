package com.biao.rebot.service;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 定义Config;
 */
@Data
public class Trade {

    /**
     * 订单号；
     */
    private String orderNo;
    /**
     * 单价；
     */
    private BigDecimal price;
    /**
     * 数量；
     */
    private BigDecimal volume;
    /**
     * 用户id;
     */
    private String userId;
    /**
     * 主交易区；
     */
    private String coinMain;
    /**
     * 交易对
     */
    private String coinOther;

    /**
     * joinSymbol string.
     *
     * @return the string
     */
    public String symbol(){
        return coinOther+coinMain;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "orderNo='" + orderNo + '\'' +
                ", price=" + price +
                ", volume=" + volume +
                ", userId='" + userId + '\'' +
                ", coinMain='" + coinMain + '\'' +
                ", coinOther='" + coinOther + '\'' +
                '}';
    }
}
