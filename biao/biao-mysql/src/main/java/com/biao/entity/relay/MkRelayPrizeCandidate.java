package com.biao.entity.relay;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("mk_relay_prize_candidate")
public class MkRelayPrizeCandidate extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("elector_id")
    private String electorId;
    @SqlField("status")
    private String status;
    @SqlField("user_id")
    private String userId;
    @SqlField("mail")
    private String mail;
    @SqlField("mobile")
    private String mobile;
    @SqlField("real_name")
    private String realName;
    @SqlField("refer_id")
    private String referId;
    @SqlField("refer_mail")
    private String referMail;
    @SqlField("refer_mobile")
    private String referMobile;
    @SqlField("refer_real_name")
    private String referRealName;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("achieve_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime achieveDate;
    @SqlField("lost_time")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime lostTime;
    @SqlField("begin_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime beginDate;
    @SqlField("end_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endDate;
    @SqlField("prize_volume")
    private BigDecimal prizeVolume;
    @SqlField("is_prize")
    private String isPrize;
    @SqlField("remark")
    private String remark;

    public String getElectorId() {
        return electorId;
    }

    public void setElectorId(String electorId) {
        this.electorId = electorId;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }

    public String getReferMail() {
        return referMail;
    }

    public void setReferMail(String referMail) {
        this.referMail = referMail;
    }

    public String getReferMobile() {
        return referMobile;
    }

    public void setReferMobile(String referMobile) {
        this.referMobile = referMobile;
    }

    public String getReferRealName() {
        return referRealName;
    }

    public void setReferRealName(String referRealName) {
        this.referRealName = referRealName;
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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public LocalDateTime getAchieveDate() {
        return achieveDate;
    }

    public void setAchieveDate(LocalDateTime achieveDate) {
        this.achieveDate = achieveDate;
    }

    public LocalDateTime getLostTime() {
        return lostTime;
    }

    public void setLostTime(LocalDateTime lostTime) {
        this.lostTime = lostTime;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrizeVolume() {
        return prizeVolume;
    }

    public void setPrizeVolume(BigDecimal prizeVolume) {
        this.prizeVolume = prizeVolume;
    }

    public String getIsPrize() {
        return isPrize;
    }

    public void setIsPrize(String isPrize) {
        this.isPrize = isPrize;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
