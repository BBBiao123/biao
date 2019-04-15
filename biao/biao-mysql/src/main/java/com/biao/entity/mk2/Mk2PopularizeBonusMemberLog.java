package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 平台运营分红日志
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_bonus_member_log")
public class Mk2PopularizeBonusMemberLog {
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

    @SqlField("total_volume")
    private BigDecimal totalVolume;

    @SqlField("bonus_ratio")
    private BigDecimal bonusRatio;

    @SqlField("income_volume")
    private BigDecimal incomeVolume;

    @SqlField("area_name")
    private String areaName;

    @SqlField("area_bonus_type")
    private String areaBonusType;

    @SqlField("join_volume")
    private BigDecimal joinVolume;

    @SqlField("join_total_volume")
    private BigDecimal joinTotalVolume;

    @SqlField("befor_income_volume")
    private BigDecimal beforIncomeVolume;

    @SqlField("bonus_date_begin")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime bonusDateBegin;

    @SqlField("bonus_date_end")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime bonusDateEnd;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;

    @SqlField("remark")
    private String remark;

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

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
    }

    public BigDecimal getBonusRatio() {
        return bonusRatio;
    }

    public void setBonusRatio(BigDecimal bonusRatio) {
        this.bonusRatio = bonusRatio;
    }

    public BigDecimal getIncomeVolume() {
        return incomeVolume;
    }

    public void setIncomeVolume(BigDecimal incomeVolume) {
        this.incomeVolume = incomeVolume;
    }

    public BigDecimal getBeforIncomeVolume() {
        return beforIncomeVolume;
    }

    public void setBeforIncomeVolume(BigDecimal beforIncomeVolume) {
        this.beforIncomeVolume = beforIncomeVolume;
    }

    public LocalDateTime getBonusDateBegin() {
        return bonusDateBegin;
    }

    public void setBonusDateBegin(LocalDateTime bonusDateBegin) {
        this.bonusDateBegin = bonusDateBegin;
    }

    public LocalDateTime getBonusDateEnd() {
        return bonusDateEnd;
    }

    public void setBonusDateEnd(LocalDateTime bonusDateEnd) {
        this.bonusDateEnd = bonusDateEnd;
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

    public BigDecimal getJoinVolume() {
        return joinVolume;
    }

    public void setJoinVolume(BigDecimal joinVolume) {
        this.joinVolume = joinVolume;
    }

    public BigDecimal getJoinTotalVolume() {
        return joinTotalVolume;
    }

    public void setJoinTotalVolume(BigDecimal joinTotalVolume) {
        this.joinTotalVolume = joinTotalVolume;
    }

    public String getAreaBonusType() {
        return areaBonusType;
    }

    public void setAreaBonusType(String areaBonusType) {
        this.areaBonusType = areaBonusType;
    }
}
