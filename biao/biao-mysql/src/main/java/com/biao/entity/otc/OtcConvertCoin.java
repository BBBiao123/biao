package com.biao.entity.otc;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.biao.sql.build.otc.OtcBaseEntity;

import java.math.BigDecimal;

/**
 * OTC资产兑换表
 */
@SqlTable("otc_convert_coin_request")
public class OtcConvertCoin extends OtcBaseEntity {

    @SqlField("request_log_id")
    private String requestLogId;

    @SqlField("user_id")
    private String userId;

    @SqlField("batch_no")
    private String batchNo;

    @SqlField("from_coin_id")
    private String fromCoinId;

    private String fromCoinSymbol;

    @SqlField("from_volume")
    private BigDecimal fromVolume;

    @SqlField("to_coin_id")
    private String toCoinId;

    private String toCoinSymbol;

    @SqlField("to_volume")
    private BigDecimal toVolume;

    @SqlField("rate")
    private BigDecimal rate;

    @SqlField("fee_volume")
    private BigDecimal feeVolume;

    @SqlField("remarks")
    private String remarks;

    @SqlField("publish_source")
    private String publishSource;

    @SqlField("status")
    private String status;

    @SqlField("result")
    private String result;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getFromCoinId() {
        return fromCoinId;
    }

    public void setFromCoinId(String fromCoinId) {
        this.fromCoinId = fromCoinId;
    }

    public BigDecimal getFromVolume() {
        return fromVolume;
    }

    public void setFromVolume(BigDecimal fromVolume) {
        this.fromVolume = fromVolume;
    }

    public String getToCoinId() {
        return toCoinId;
    }

    public void setToCoinId(String toCoinId) {
        this.toCoinId = toCoinId;
    }

    public BigDecimal getToVolume() {
        return toVolume;
    }

    public void setToVolume(BigDecimal toVolume) {
        this.toVolume = toVolume;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getFeeVolume() {
        return feeVolume;
    }

    public void setFeeVolume(BigDecimal feeVolume) {
        this.feeVolume = feeVolume;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPublishSource() {
        return publishSource;
    }

    public void setPublishSource(String publishSource) {
        this.publishSource = publishSource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFromCoinSymbol() {
        return fromCoinSymbol;
    }

    public void setFromCoinSymbol(String fromCoinSymbol) {
        this.fromCoinSymbol = fromCoinSymbol;
    }

    public String getToCoinSymbol() {
        return toCoinSymbol;
    }

    public void setToCoinSymbol(String toCoinSymbol) {
        this.toCoinSymbol = toCoinSymbol;
    }

    public String getRequestLogId() {
        return requestLogId;
    }

    public void setRequestLogId(String requestLogId) {
        this.requestLogId = requestLogId;
    }
}
