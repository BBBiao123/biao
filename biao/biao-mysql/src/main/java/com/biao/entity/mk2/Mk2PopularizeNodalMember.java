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
@SqlTable("mk2_popularize_nodal_member")
public class Mk2PopularizeNodalMember {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

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

    @SqlField("lock_volume")
    private BigDecimal lockVolume;

    @SqlField("release_volume")
    private BigDecimal releaseVolume;

    @SqlField("release_begin_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime releaseBeginDate;

    @SqlField("release_cycle")
    private String releaseCycle;

    @SqlField("release_cycle_ratio")
    private BigDecimal releaseCycleRatio;

    @SqlField("release_over")
    private String releaseOver;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;

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
}
