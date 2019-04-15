package com.biao.entity.relay;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("mk_relay_auto_record")
public class MkRelayAutoRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("status")
    private String status;
    @SqlField("begin_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime beginDate;
    @SqlField("end_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endDate;
    @SqlField("user_id")
    private String userId;
    @SqlField("mail")
    private String mail;
    @SqlField("mobile")
    private String mobile;
    @SqlField("real_name")
    private String realName;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("reach_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime reachDate;
    @SqlField("candidate_id")
    private String candidateId;
    @SqlField("remark")
    private String remark;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
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

    public String getRemark() {
        return remark;
    }

    public LocalDateTime getReachDate() {
        return reachDate;
    }

    public void setReachDate(LocalDateTime reachDate) {
        this.reachDate = reachDate;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
