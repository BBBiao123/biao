package com.biao.entity;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户c2c资产变更日志
 */
@SqlTable("js_plat_offline_coin_volume_log")
public class OfflineCoinVolumeLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("offline_volume_id")
    private String offlineVolumeId;

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
    @SqlField("batch_no")
    private String batchNo;
    @SqlField("remark")
    private String remark;
    @SqlField("create_by")
    private String createBy;
    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime createDate;

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime updateDate;

    @SqlField("version")
    private Long version;

    @SqlField("otc_advert_volume")
    private BigDecimal otcAdvertVolume;
    @SqlField("otc_lock_volume")
    private BigDecimal otcLockVolume;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public BigDecimal getAdvertVolume() {
        return advertVolume;
    }

    public void setAdvertVolume(BigDecimal advertVolume) {
        this.advertVolume = advertVolume;
    }

    public BigDecimal getLockVolume() {
        return lockVolume;
    }

    public void setLockVolume(BigDecimal lockVolume) {
        this.lockVolume = lockVolume;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getOfflineVolumeId() {
        return offlineVolumeId;
    }

    public void setOfflineVolumeId(String offlineVolumeId) {
        this.offlineVolumeId = offlineVolumeId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public BigDecimal getBailVolume() {
        return bailVolume;
    }

    public void setBailVolume(BigDecimal bailVolume) {
        this.bailVolume = bailVolume;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
