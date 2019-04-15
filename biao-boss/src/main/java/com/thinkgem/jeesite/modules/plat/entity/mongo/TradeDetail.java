package com.thinkgem.jeesite.modules.plat.entity.mongo;


import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 * @version 1.0
 * @date 2018/4/27 17:15
 * @since JDK 1.8
 */
@Document
public class TradeDetail {



    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 交易号；
     */
    private String tradeNo;

    /**
     * 时间
     */
    private Date tradeTime;


    /**
     * 买入 / 卖出
     */
    private Integer type;

    /**
     * 价格
     */
    private BigDecimal price;

    public BigDecimal getExFee() {
        return exFee;
    }

    public void setExFee(BigDecimal exFee) {
        this.exFee = exFee;
    }

    /**
     * 手续费
     */
    private BigDecimal exFee;



    /**
     * 成交数量
     */
    private BigDecimal volume;



    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Date getTradeTime() {
        return this.tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }


    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    /**
     * 🐽主区 币代号  eth_btc 这里就是btc
     */
    private String coinMain;

    /**
     * 其他币种代号 eth_btc 这里存的就是eth
     */
    private String coinOther;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public String getCoinMain() {
        return coinMain;
    }

    public void setCoinMain(String coinMain) {
        this.coinMain = coinMain;
    }

    public String getCoinOther() {
        return coinOther;
    }

    public void setCoinOther(String coinOther) {
        this.coinOther = coinOther;
    }
}
