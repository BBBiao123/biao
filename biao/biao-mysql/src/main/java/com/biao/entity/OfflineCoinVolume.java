package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 用户c2c资产
 */
@SqlTable("js_plat_offline_coin_volume")
public class OfflineCoinVolume extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("advert_volume")
    private BigDecimal advertVolume;
    @SqlField("lock_volume")
    private BigDecimal lockVolume;
    @SqlField("bail_volume")
    private BigDecimal bailVolume;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("version")
    private Long version;

    @SqlField("otc_advert_volume")
    private BigDecimal otcAdvertVolume = BigDecimal.ZERO;
    @SqlField("otc_lock_volume")
    private BigDecimal otcLockVolume = BigDecimal.ZERO;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getLockVolume() {
        return lockVolume;
    }

    public void setLockVolume(BigDecimal lockVolume) {
        this.lockVolume = lockVolume;
    }

    public BigDecimal getAdvertVolume() {
        return advertVolume;
    }

    public void setAdvertVolume(BigDecimal advertVolume) {
        this.advertVolume = advertVolume;
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

    public BigDecimal getBailVolume() {
        return bailVolume;
    }

    public void setBailVolume(BigDecimal bailVolume) {
        this.bailVolume = bailVolume;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getOtcAdvertVolume() {
        return otcAdvertVolume;
    }

    public void setOtcAdvertVolume(BigDecimal otcAdvertVolume) {
        this.otcAdvertVolume = otcAdvertVolume;
    }

    public BigDecimal getOtcLockVolume() {
        return otcLockVolume;
    }

    public void setOtcLockVolume(BigDecimal otcLockVolume) {
        this.otcLockVolume = otcLockVolume;
    }
}
