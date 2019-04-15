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
 * 资产变更实体
 */
@SqlTable("otc_volume_change_request")
public class OtcVolumeChangeRequest implements Serializable {

    @PrimaryKey(insertIsSkip=false)
    @SqlField("id")
    protected String id ;

    @SqlField("create_by")
    protected String createBy ;

    @SqlField("update_by")
    protected String updateBy ;

    @SqlField("create_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime createDate ;

    @SqlField("update_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime updateDate ;

    @SqlField("type")
    private String type;
    @SqlField("request_log_id")
    private String requestLogId;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("fee_volume")
    private BigDecimal feeVolume;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("symbol")
    private String symbol;
    @SqlField("publish_source")
    private String publishSource;
    @SqlField("batch_no")
    private String batchNo;
    @SqlField("buy_user_id")
    private String buyUserId;
    @SqlField("sell_user_id")
    private String sellUserId;
    @SqlField("order_id")
    private String orderId;
    @SqlField("ad_type")
    private String adType;
    @SqlField("sub_order_id")
    private String subOrderId;
    @SqlField("status")
    private String status;
    @SqlField("fee_deduct_type")
    private String feeDeductType;
    @SqlField("remarks")
    private String remarks;
    @SqlField("result")
    private String result;
    @SqlField("login_user_id")
    private String loginUserId;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestLogId() {
        return requestLogId;
    }

    public void setRequestLogId(String requestLogId) {
        this.requestLogId = requestLogId;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

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

    public String getBuyUserId() {
        return buyUserId;
    }

    public void setBuyUserId(String buyUserId) {
        this.buyUserId = buyUserId;
    }

    public String getSellUserId() {
        return sellUserId;
    }

    public void setSellUserId(String sellUserId) {
        this.sellUserId = sellUserId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeeDeductType() {
        return feeDeductType;
    }

    public void setFeeDeductType(String feeDeductType) {
        this.feeDeductType = feeDeductType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }
}
