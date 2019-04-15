package com.biao.entity.lucky;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("mk_lucky_draw_player")
public class MkLuckyDrawPlayer extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("periods")
    private Integer periods;
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
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("deduct_fee")
    private BigDecimal deductFee;
    @SqlField("lucky_volume")
    private BigDecimal luckyVolume;
    @SqlField("draw_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime drawDate;
    @SqlField("remark")
    private String remark;

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
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

    public BigDecimal getDeductFee() {
        return deductFee;
    }

    public void setDeductFee(BigDecimal deductFee) {
        this.deductFee = deductFee;
    }

    public BigDecimal getLuckyVolume() {
        return luckyVolume;
    }

    public void setLuckyVolume(BigDecimal luckyVolume) {
        this.luckyVolume = luckyVolume;
    }

    public LocalDateTime getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(LocalDateTime drawDate) {
        this.drawDate = drawDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
