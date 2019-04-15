package com.biao.entity.otc;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * OTC广告
 */
@SqlTable("otc_offline_order")
public class OtcOfflineOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @SqlField("support_pay")
    private String supportPay;
    @SqlField("support_currency_code")
    private String supportCurrencyCode;
    @SqlField("publish_source")
    private String publishSource;
    @SqlField("user_id")
    private String userId;
    @SqlField("tag")
    private String tag;
    @SqlField("real_name")
    private String realName;
    @SqlField("mobile")
    private String mobile;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("symbol")
    private String symbol;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("lock_volume")
    private BigDecimal lockVolume;
    @SqlField("success_volume")
    private BigDecimal successVolume;
    @SqlField("price")
    private BigDecimal price;
    @SqlField("total_price")
    private BigDecimal totalPrice;
    @SqlField("min_volume")
    private BigDecimal minVolume;
    @SqlField("max_volume")
    private BigDecimal maxVolume;
    @SqlField("fee_volume")
    private BigDecimal feeVolume;
    @SqlField("status")
    private String status;
    @SqlField("flag")
    private String flag;
    @SqlField("ex_type")
    private String exType;
    @SqlField("remarks")
    private String remarks;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @SqlField("cancel_date")
    private LocalDateTime cancelDate;

    public String getSupportPay() {
        return supportPay;
    }

    public void setSupportPay(String supportPay) {
        this.supportPay = supportPay;
    }

    public String getSupportCurrencyCode() {
        return supportCurrencyCode;
    }

    public void setSupportCurrencyCode(String supportCurrencyCode) {
        this.supportCurrencyCode = supportCurrencyCode;
    }

    public String getPublishSource() {
        return publishSource;
    }

    public void setPublishSource(String publishSource) {
        this.publishSource = publishSource;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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

    public BigDecimal getSuccessVolume() {
        return successVolume;
    }

    public void setSuccessVolume(BigDecimal successVolume) {
        this.successVolume = successVolume;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(BigDecimal minVolume) {
        this.minVolume = minVolume;
    }

    public BigDecimal getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(BigDecimal maxVolume) {
        this.maxVolume = maxVolume;
    }

    public BigDecimal getFeeVolume() {
        return feeVolume;
    }

    public void setFeeVolume(BigDecimal feeVolume) {
        this.feeVolume = feeVolume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getExType() {
        return exType;
    }

    public void setExType(String exType) {
        this.exType = exType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }
}
