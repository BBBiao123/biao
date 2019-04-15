package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeDeserializer;
import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 冻结数量释放记录
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_release_log")
public class Mk2PopularizeReleaseLog {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("type")
    private String type;

    @SqlField("relation_id")
    private String relationId;

    @SqlField("user_id")
    private String userId;

    @SqlField("mail")
    private String mail;

    @SqlField("mobile")
    private String mobile;

    @SqlField("coin_id")
    private String coinId;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("release_volume")
    private BigDecimal releaseVolume;

    @SqlField("release_cycle_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime releaseCycleDate;

    @SqlField("release_status")
    private String releaseStatus;

    @SqlField("release_cycle_ratio")
    private BigDecimal releaseCycleRatio;

    @SqlField("release_source")
    private String releaseSource;

    @SqlField("area_name")
    private String areaName;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createDate;

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateDate;

    @SqlField("remark")
    private String remark;

    @SqlField("release_version")
    private Long releaseVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public BigDecimal getReleaseVolume() {
        return releaseVolume;
    }

    public void setReleaseVolume(BigDecimal releaseVolume) {
        this.releaseVolume = releaseVolume;
    }

    public LocalDateTime getReleaseCycleDate() {
        return releaseCycleDate;
    }

    public void setReleaseCycleDate(LocalDateTime releaseCycleDate) {
        this.releaseCycleDate = releaseCycleDate;
    }

    public String getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(Long releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public BigDecimal getReleaseCycleRatio() {
        return releaseCycleRatio;
    }

    public void setReleaseCycleRatio(BigDecimal releaseCycleRatio) {
        this.releaseCycleRatio = releaseCycleRatio;
    }

    public String getReleaseSource() {
        return releaseSource;
    }

    public void setReleaseSource(String releaseSource) {
        this.releaseSource = releaseSource;
    }
}
