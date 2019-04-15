package com.biao.entity.otc;

import com.biao.config.CustomLocalDateTimeDeserializer;
import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * OTC通兑订单表
 */
@SqlTable("otc_exchange_request")
public class OtcExchangeOrder implements Serializable {

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("create_by")
    protected String createBy;

    @SqlField("update_by")
    protected String updateBy;

    @SqlField("create_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime createDate;

    @SqlField("update_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime updateDate;

    @SqlField("publish_source")
    private String publishSource;
    @SqlField("ip")
    private String ip;
    @SqlField("batch_no")
    private String batchNo;
    @SqlField("symbol")
    private String symbol;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("real_volume")
    private BigDecimal realVolume;
    @SqlField("user_id")
    private String userId;
    @SqlField("real_name")
    private String realName;
    @SqlField("mobile")
    private String mobile;
    @SqlField("mail")
    private String mail;
    @SqlField("fee_volume")
    private BigDecimal feeVolume;
    @SqlField("ask_user_mobile")
    private String askUserMobile;
    @SqlField("ask_user_mail")
    private String askUserMail;
    @SqlField("ask_real_name")
    private String askRealName;
    @SqlField("ask_user_id")
    private String askUserId;
    @SqlField("ask_fee_volume")
    private BigDecimal askFeeVolume;
    @SqlField("remarks")
    private String remarks;
    @SqlField("status")
    private String status;
    @SqlField("result")
    private String result;

    public String getPublishSource() {
        return publishSource;
    }

    public void setPublishSource(String publishSource) {
        this.publishSource = publishSource;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getRealVolume() {
        return realVolume;
    }

    public void setRealVolume(BigDecimal realVolume) {
        this.realVolume = realVolume;
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

    public BigDecimal getFeeVolume() {
        return feeVolume;
    }

    public void setFeeVolume(BigDecimal feeVolume) {
        this.feeVolume = feeVolume;
    }

    public String getAskUserMobile() {
        return askUserMobile;
    }

    public void setAskUserMobile(String askUserMobile) {
        this.askUserMobile = askUserMobile;
    }

    public String getAskRealName() {
        return askRealName;
    }

    public void setAskRealName(String askRealName) {
        this.askRealName = askRealName;
    }

    public String getAskUserId() {
        return askUserId;
    }

    public void setAskUserId(String askUserId) {
        this.askUserId = askUserId;
    }

    public BigDecimal getAskFeeVolume() {
        return askFeeVolume;
    }

    public void setAskFeeVolume(BigDecimal askFeeVolume) {
        this.askFeeVolume = askFeeVolume;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAskUserMail() {
        return askUserMail;
    }

    public void setAskUserMail(String askUserMail) {
        this.askUserMail = askUserMail;
    }
}
