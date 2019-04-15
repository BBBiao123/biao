package com.biao.entity.otc;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * otc广告变更流水
 */
@SqlTable("otc_offline_order_log")
public class OtcOfflineOrderLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;
    @SqlField("order_id")
    private String orderId;
    @SqlField("support_pay")
    private String supportPay;
    @SqlField("support_currency_code")
    private String supportCurrencyCode;
    @SqlField("publish_source")
    private String publishSource;
    @SqlField("user_id")
    private String userId;
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
    @SqlField("status")
    private String status;
    @SqlField("flag")
    private String flag;
    @SqlField("fee_volume")
    private String feeVolume;
    @SqlField("remarks")
    private String remarks;
    @SqlField("batch_no")
    private String batchNo;
    @SqlField("create_by")
    protected String createBy;
    @SqlField("update_date")
    private LocalDateTime updateDate;
    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime createDate;
    @SqlField("cancel_date")
    private LocalDateTime cancelDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getFeeVolume() {
        return feeVolume;
    }

    public void setFeeVolume(String feeVolume) {
        this.feeVolume = feeVolume;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }
}
