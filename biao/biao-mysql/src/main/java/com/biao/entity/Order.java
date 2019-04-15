package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 用户挂单表
 */
@SqlTable("js_plat_ex_order")
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("ask_volume")
    private BigDecimal askVolume;
    @SqlField("success_volume")
    private BigDecimal successVolume;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("ask_coin_id")
    private String askCoinId;
    @SqlField("to_coin_symbol")
    private String toCoinSymbol;
    @SqlField("to_coin_volume")
    private BigDecimal toCoinVolume;
    @SqlField("ex_fee")
    private BigDecimal exFee;
    @SqlField("ex_type")
    private Integer exType;
    @SqlField("status")
    private Integer status;
    @SqlField("flag")
    private Integer flag;
    @SqlField("price")
    private BigDecimal price;
    @SqlField("coin_main")
    private String coinMain;
    @SqlField("coin_other")
    private String coinOther;

    @SqlField("spent_volume")
    private BigDecimal spentVolume;
    @SqlField("cancel_lock")
    private String cancelLock;

    public BigDecimal getSpentVolume() {
        return spentVolume;
    }

    public Order setSpentVolume(BigDecimal spentVolume) {
        this.spentVolume = spentVolume;
        return this;
    }

    public String getCoinMain() {
        return coinMain;
    }

    public Order setCoinMain(String coinMain) {
        this.coinMain = coinMain;
        return this;
    }

    public String getCoinOther() {
        return coinOther;
    }

    public Order setCoinOther(String coinOther) {
        this.coinOther = coinOther;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Order setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Order setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public BigDecimal getAskVolume() {
        return askVolume;
    }

    public Order setAskVolume(BigDecimal askVolume) {
        this.askVolume = askVolume;
        return this;
    }

    public BigDecimal getSuccessVolume() {
        return successVolume;
    }

    public Order setSuccessVolume(BigDecimal successVolume) {
        this.successVolume = successVolume;
        return this;
    }

    public String getCoinId() {
        return coinId;
    }

    public Order setCoinId(String coinId) {
        this.coinId = coinId;
        return this;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public Order setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
        return this;
    }

    public String getAskCoinId() {
        return askCoinId;
    }

    public Order setAskCoinId(String askCoinId) {
        this.askCoinId = askCoinId;
        return this;
    }

    public String getToCoinSymbol() {
        return toCoinSymbol;
    }

    public Order setToCoinSymbol(String toCoinSymbol) {
        this.toCoinSymbol = toCoinSymbol;
        return this;
    }

    public BigDecimal getToCoinVolume() {
        return toCoinVolume;
    }

    public Order setToCoinVolume(BigDecimal toCoinVolume) {
        this.toCoinVolume = toCoinVolume;
        return this;
    }

    public BigDecimal getExFee() {
        return exFee;
    }

    public Order setExFee(BigDecimal exFee) {
        this.exFee = exFee;
        return this;
    }

    public Integer getExType() {
        return exType;
    }

    public Order setExType(Integer exType) {
        this.exType = exType;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public Order setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getFlag() {
        return flag;
    }

    public Order setFlag(Integer flag) {
        this.flag = flag;
        return this;
    }

    public String getCancelLock() {
        return cancelLock;
    }

    public Order setCancelLock(String cancelLock) {
        this.cancelLock = cancelLock;
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                "userId='" + userId + '\'' +
                ", askVolume=" + askVolume +
                ", successVolume=" + successVolume +
                ", coinSymbol='" + coinSymbol + '\'' +
                ", toCoinSymbol='" + toCoinSymbol + '\'' +
                ", toCoinVolume=" + toCoinVolume +
                ", exFee=" + exFee +
                ", exType=" + exType +
                ", status=" + status +
                ", price=" + price +
                ", coinMain='" + coinMain + '\'' +
                ", coinOther='" + coinOther + '\'' +
                ", spentVolume=" + spentVolume +
                '}';
    }
}
