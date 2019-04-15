package com.biao.entity.lucky;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

@SqlTable("mk_lucky_draw_record")
public class MkLuckyDrawRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("status")
    private String status;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("pool_volume")
    private BigDecimal poolVolume;
    @SqlField("grant_volume")
    private BigDecimal grantVolume;
    @SqlField("deduct_fee")
    private BigDecimal deductFee;
    @SqlField("player_number")
    private Integer playerNumber;
    @SqlField("prize_volume")
    private BigDecimal prizeVolume;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("user_id")
    private String userId;
    @SqlField("mail")
    private String mail;
    @SqlField("mobile")
    private String mobile;
    @SqlField("real_name")
    private String realName;
    @SqlField("remark")
    private String remark;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getPoolVolume() {
        return poolVolume;
    }

    public void setPoolVolume(BigDecimal poolVolume) {
        this.poolVolume = poolVolume;
    }

    public BigDecimal getGrantVolume() {
        return grantVolume;
    }

    public void setGrantVolume(BigDecimal grantVolume) {
        this.grantVolume = grantVolume;
    }

    public BigDecimal getDeductFee() {
        return deductFee;
    }

    public void setDeductFee(BigDecimal deductFee) {
        this.deductFee = deductFee;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }

    public BigDecimal getPrizeVolume() {
        return prizeVolume;
    }

    public void setPrizeVolume(BigDecimal prizeVolume) {
        this.prizeVolume = prizeVolume;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
