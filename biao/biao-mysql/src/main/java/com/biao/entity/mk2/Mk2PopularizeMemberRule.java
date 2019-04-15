package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员规则表
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_member_rule")
public class Mk2PopularizeMemberRule {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("type")
    private String type;

    @SqlField("release_open")
    private String releaseOpen;

    @SqlField("release_version")
    private Long releaseVersion;

    @SqlField("release_day")
    private String releaseDay;

    @SqlField("release_week")
    private String releaseWeek;

    @SqlField("release_month")
    private String releaseMonth;

    @SqlField("release_year")
    private String releaseYear;

    @SqlField("release_type")
    private String releaseType;

    @SqlField("total_member")
    private Long totalMember;

    @SqlField("sold_member")
    private Long soldMember;

    @SqlField("lock_volume")
    private BigDecimal lockVolume;

    @SqlField("bonus_ratio")
    private BigDecimal bonusRatio;

    @SqlField("phone_bonus_ratio")
    private BigDecimal phoneBonusRatio;

    @SqlField("refer_bonus_ratio")
    private BigDecimal referBonusRatio;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(Long totalMember) {
        this.totalMember = totalMember;
    }

    public Long getSoldMember() {
        return soldMember;
    }

    public void setSoldMember(Long soldMember) {
        this.soldMember = soldMember;
    }

    public BigDecimal getLockVolume() {
        return lockVolume;
    }

    public void setLockVolume(BigDecimal lockVolume) {
        this.lockVolume = lockVolume;
    }

    public BigDecimal getBonusRatio() {
        return bonusRatio;
    }

    public void setBonusRatio(BigDecimal bonusRatio) {
        this.bonusRatio = bonusRatio;
    }

    public BigDecimal getPhoneBonusRatio() {
        return phoneBonusRatio;
    }

    public void setPhoneBonusRatio(BigDecimal phoneBonusRatio) {
        this.phoneBonusRatio = phoneBonusRatio;
    }

    public BigDecimal getReferBonusRatio() {
        return referBonusRatio;
    }

    public void setReferBonusRatio(BigDecimal referBonusRatio) {
        this.referBonusRatio = referBonusRatio;
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

    public String getReleaseOpen() {
        return releaseOpen;
    }

    public void setReleaseOpen(String releaseOpen) {
        this.releaseOpen = releaseOpen;
    }

    public Long getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(Long releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public String getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(String releaseDay) {
        this.releaseDay = releaseDay;
    }

    public String getReleaseWeek() {
        return releaseWeek;
    }

    public void setReleaseWeek(String releaseWeek) {
        this.releaseWeek = releaseWeek;
    }

    public String getReleaseMonth() {
        return releaseMonth;
    }

    public void setReleaseMonth(String releaseMonth) {
        this.releaseMonth = releaseMonth;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    @Override
    public String toString() {
        return "Mk2PopularizeMemberRule{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", releaseOpen='" + releaseOpen + '\'' +
                ", releaseVersion=" + releaseVersion +
                ", releaseDay='" + releaseDay + '\'' +
                ", releaseWeek='" + releaseWeek + '\'' +
                ", releaseMonth='" + releaseMonth + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", releaseType='" + releaseType + '\'' +
                ", totalMember=" + totalMember +
                ", soldMember=" + soldMember +
                ", lockVolume=" + lockVolume +
                ", bonusRatio=" + bonusRatio +
                ", phoneBonusRatio=" + phoneBonusRatio +
                ", referBonusRatio=" + referBonusRatio +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
