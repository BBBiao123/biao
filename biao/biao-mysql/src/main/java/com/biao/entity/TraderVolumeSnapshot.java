package com.biao.entity;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("js_plat_trader_volume_snapshot")
public class TraderVolumeSnapshot extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("snap_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime snapDate;
    @SqlField("user_id")
    private String userId;
    @SqlField("user_tag")
    private String userTag;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("trade_volume")
    private BigDecimal tradeVolume;
    @SqlField("offline_volume")
    private BigDecimal offlineVolume;
    @SqlField("lock_volume")
    private BigDecimal lockVolume;
    @SqlField("total_volume")
    private BigDecimal totalVolume;
    @SqlField("bill_sum_volume")
    private BigDecimal billSumVolume;
    @SqlField("balance")
    private BigDecimal balance;
    @SqlField("bobi_volume")
    private BigDecimal bobiVolume;
    @SqlField("remark")
    private String remark;

    public LocalDateTime getSnapDate() {


        return snapDate;
    }

    public void setSnapDate(LocalDateTime snapDate) {
        this.snapDate = snapDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
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

    public BigDecimal getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(BigDecimal tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public BigDecimal getOfflineVolume() {
        return offlineVolume;
    }

    public void setOfflineVolume(BigDecimal offlineVolume) {
        this.offlineVolume = offlineVolume;
    }

    public BigDecimal getLockVolume() {
        return lockVolume;
    }

    public void setLockVolume(BigDecimal lockVolume) {
        this.lockVolume = lockVolume;
    }

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
    }

    public BigDecimal getBillSumVolume() {
        return billSumVolume;
    }

    public void setBillSumVolume(BigDecimal billSumVolume) {
        this.billSumVolume = billSumVolume;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBobiVolume() {
        return bobiVolume;
    }

    public void setBobiVolume(BigDecimal bobiVolume) {
        this.bobiVolume = bobiVolume;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
