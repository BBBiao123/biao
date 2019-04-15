package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 用户挂单成交详情表
 */
@SqlTable("js_plat_ex_order_detail")
public class OrderDetail extends BaseEntity {

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
    @SqlField("to_coin_id")
    private String toCoinId;
    @SqlField("coin_symbol")
    private String toCoinSymbol;
    @SqlField("to_coin_vlolume")
    private String toCoinVolume;
    @SqlField("ex_fee")
    private BigDecimal exFee;
    @SqlField("ex_type")
    private BigDecimal exType;
    @SqlField("status")
    private Integer status;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAskVolume() {
        return askVolume;
    }

    public void setAskVolume(BigDecimal askVolume) {
        this.askVolume = askVolume;
    }

    public BigDecimal getSuccessVolume() {
        return successVolume;
    }

    public void setSuccessVolume(BigDecimal successVolume) {
        this.successVolume = successVolume;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getToCoinId() {
        return toCoinId;
    }

    public void setToCoinId(String toCoinId) {
        this.toCoinId = toCoinId;
    }

    public String getToCoinSymbol() {
        return toCoinSymbol;
    }

    public void setToCoinSymbol(String toCoinSymbol) {
        this.toCoinSymbol = toCoinSymbol;
    }

    public String getToCoinVolume() {
        return toCoinVolume;
    }

    public void setToCoinVolume(String toCoinVolume) {
        this.toCoinVolume = toCoinVolume;
    }

    public BigDecimal getExFee() {
        return exFee;
    }

    public void setExFee(BigDecimal exFee) {
        this.exFee = exFee;
    }

    public BigDecimal getExType() {
        return exType;
    }

    public void setExType(BigDecimal exType) {
        this.exType = exType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
