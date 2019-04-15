package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 用户c2c挂单费用 目前只收卖出方手续费
 */
@SqlTable("js_plat_offline_order_fee")
public class OfflineOrderFee extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("order_id")
    private String orderId;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("fee_volume")
    private BigDecimal feeVolume;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("ex_type")
    private Integer exType;
    @SqlField("status")
    private Integer status;
    @SqlField("remarks")
    private String remarks;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getFeeVolume() {
        return feeVolume;
    }

    public void setFeeVolume(BigDecimal feeVolume) {
        this.feeVolume = feeVolume;
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

    public Integer getExType() {
        return exType;
    }

    public void setExType(Integer exType) {
        this.exType = exType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
