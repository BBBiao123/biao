package com.biao.entity.otc;

import com.biao.config.CustomLocalDateTimeDeserializer;
import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 拨币请求实体
 */
@SqlTable("otc_appropriation_request")
public class OtcAppropriationRequest implements Serializable {

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

    @SqlField("coin_id")
    private String coinId;
    @SqlField("symbol")
    private String symbol;
    @SqlField("publish_source")
    private String publishSource;
    @SqlField("batch_no")
    private String batchNo;
    @SqlField("user_id_str")
    private String userIdStr;
    @SqlField("volume_str")
    private String volumeStr;
    @SqlField("status")
    private String status;
    @SqlField("remarks")
    private String remarks;
    @SqlField("result")
    private String result;
    @SqlField("type")
    private String type;


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

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getVolumeStr() {
        return volumeStr;
    }

    public void setVolumeStr(String volumeStr) {
        this.volumeStr = volumeStr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
