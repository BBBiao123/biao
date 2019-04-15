package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 用户coin 提现记录
 */
@SqlTable("js_plat_offline_transfer_log")
public class OfflineTransferLog extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("fee_volume")
    private BigDecimal feeVolume = BigDecimal.ZERO;
    @SqlField("type")
    private Integer type;

    @SqlField("source_volume")
    private BigDecimal sourceVolume;// 划转前from账户资产镜像

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public BigDecimal getSourceVolume() {
        return sourceVolume;
    }

    public void setSourceVolume(BigDecimal sourceVolume) {
        this.sourceVolume = sourceVolume;
    }
}
