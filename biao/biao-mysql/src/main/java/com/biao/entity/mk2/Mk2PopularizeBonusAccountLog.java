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
@SqlTable("mk2_popularize_bonus_account_log")
public class Mk2PopularizeBonusAccountLog {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("user_id")
    private String userId;        //

    @SqlField("mail")
    private String mail;        //

    @SqlField("mobile")
    private String mobile;        //

    @SqlField("id_card")
    private String idCard;        //

    @SqlField("real_name")
    private String realName;        //

    @SqlField("bonus_date_begin")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime bonusDateBegin;        //

    @SqlField("bonus_date_end")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime bonusDateEnd;        //

    @SqlField("coin_id")
    private String coinId;        //

    @SqlField("coin_symbol")
    private String coinSymbol;        //

    @SqlField("income_volume")
    private BigDecimal incomeVolume;        //

    @SqlField("befor_income_volume")
    private BigDecimal beforIncomeVolume;        //

    @SqlField("status")
    private String status;        //

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;        //

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;        //

    @SqlField("remark")
    private String remark;        //

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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
