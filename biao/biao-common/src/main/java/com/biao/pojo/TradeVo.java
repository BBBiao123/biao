package com.biao.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易买入、买出的数据传输对象;
 *
 *  ""
 */
public class TradeVo implements Serializable {
    /**
     * 用户ID;
     */
    private String userId;
    /**
     * 订单号；
     */
    private String orderNo;
    /**
     * 交易数量
     */
    private BigDecimal volume;
    /**
     * 交易单价;
     */
    private BigDecimal price;
    /**
     * 买入、买出的币种
     */
    private String coinOther;
    /**
     * 对应id;
     */
    @JsonIgnore
    private transient String coinOtherId;
    /**
     * 交易对币总;
     */
    private String coinMain;
    /**
     * 交易id;
     */
    @JsonIgnore
    private transient String coinManId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCoinOther() {
        return coinOther;
    }

    public void setCoinOther(String coinOther) {
        this.coinOther = coinOther;
    }

    public String getCoinMain() {
        return coinMain;
    }

    public void setCoinMain(String coinMain) {
        this.coinMain = coinMain;
    }

    public String getCoinOtherId() {
        return coinOtherId;
    }

    public void setCoinOtherId(String coinOtherId) {
        this.coinOtherId = coinOtherId;
    }

    public String getCoinManId() {
        return coinManId;
    }

    public void setCoinManId(String coinManId) {
        this.coinManId = coinManId;
    }

    @Override
    public String toString() {
        return "TradeVo{" +
                "userId='" + userId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", count=" + volume +
                ", price=" + price +
                ", coinOther='" + coinOther + '\'' +
                ", coinOtherId='" + coinOtherId + '\'' +
                ", coinMain='" + coinMain + '\'' +
                ", coinManId='" + coinManId + '\'' +
                '}';
    }
}
