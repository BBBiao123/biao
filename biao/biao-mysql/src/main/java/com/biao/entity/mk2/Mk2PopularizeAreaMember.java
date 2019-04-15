package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 区域合伙人售卖规则
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_area_member")
public class Mk2PopularizeAreaMember {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("type")
    private String type;        //

    @SqlField("parent_id")
    private String parentId;

    @SqlField("ratio")
    private BigDecimal ratio;

    @SqlField("coin_id")
    private String coinId;        //

    @SqlField("coin_symbol")
    private String coinSymbol;        //

    @SqlField("area_id")
    private String areaId;        //

    @SqlField("area_name")
    private String areaName;        //

    @SqlField("area_paraent_id")
    private String areaParaentId;        //

    @SqlField("area_paraent_name")
    private String areaParaentName;        //

    @SqlField("status")
    private String status;        //

    @SqlField("user_id")
    private String userId;        //

    @SqlField("mail")
    private String mail;        //

    @SqlField("mobile")
    private String mobile;        //

    @SqlField("lock_volume")
    private BigDecimal lockVolume;        //

    @SqlField("release_volume")
    private BigDecimal releaseVolume;        //

    @SqlField("release_begin_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime releaseBeginDate;        //

    @SqlField("release_cycle")
    private String releaseCycle;        //

    @SqlField("release_cycle_ratio")
    private BigDecimal releaseCycleRatio;        //

    @SqlField("release_over")
    private String releaseOver;        //

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;        //

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;        //


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

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaParaentId() {
        return areaParaentId;
    }

    public void setAreaParaentId(String areaParaentId) {
        this.areaParaentId = areaParaentId;
    }

    public String getAreaParaentName() {
        return areaParaentName;
    }

    public void setAreaParaentName(String areaParaentName) {
        this.areaParaentName = areaParaentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public BigDecimal getLockVolume() {
        return lockVolume;
    }

    public void setLockVolume(BigDecimal lockVolume) {
        this.lockVolume = lockVolume;
    }

    public BigDecimal getReleaseVolume() {
        return releaseVolume;
    }

    public void setReleaseVolume(BigDecimal releaseVolume) {
        this.releaseVolume = releaseVolume;
    }

    public LocalDateTime getReleaseBeginDate() {
        return releaseBeginDate;
    }

    public void setReleaseBeginDate(LocalDateTime releaseBeginDate) {
        this.releaseBeginDate = releaseBeginDate;
    }

    public String getReleaseCycle() {
        return releaseCycle;
    }

    public void setReleaseCycle(String releaseCycle) {
        this.releaseCycle = releaseCycle;
    }

    public BigDecimal getReleaseCycleRatio() {
        return releaseCycleRatio;
    }

    public void setReleaseCycleRatio(BigDecimal releaseCycleRatio) {
        this.releaseCycleRatio = releaseCycleRatio;
    }

    public String getReleaseOver() {
        return releaseOver;
    }

    public void setReleaseOver(String releaseOver) {
        this.releaseOver = releaseOver;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }
}
