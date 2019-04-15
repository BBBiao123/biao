package com.biao.entity.relay;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

@SqlTable("mk_relay_prize_config")
public class MkRelayPrizeConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("name")
    private String name;
    @SqlField("status")
    private String status;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("start_volume")
    private BigDecimal startVolume;
    @SqlField("step_add_volume")
    private BigDecimal stepAddVolume;
    @SqlField("begin_time")
    private String beginTime;
    @SqlField("end_time")
    private String endTime;
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
    @SqlField("is_remit")
    private String isRemit;
    @SqlField("min_volume")
    private BigDecimal minVolume;
    @SqlField("grant_volume")
    private BigDecimal grantVolume;
    @SqlField("cur_pool_volume")
    private BigDecimal curPoolVolume;
    @SqlField("remark")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public BigDecimal getStartVolume() {
        return startVolume;
    }

    public void setStartVolume(BigDecimal startVolume) {
        this.startVolume = startVolume;
    }

    public BigDecimal getStepAddVolume() {
        return stepAddVolume;
    }

    public void setStepAddVolume(BigDecimal stepAddVolume) {
        this.stepAddVolume = stepAddVolume;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getIsRemit() {
        return isRemit;
    }

    public void setIsRemit(String isRemit) {
        this.isRemit = isRemit;
    }

    public BigDecimal getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(BigDecimal minVolume) {
        this.minVolume = minVolume;
    }

    public BigDecimal getGrantVolume() {
        return grantVolume;
    }

    public void setGrantVolume(BigDecimal grantVolume) {
        this.grantVolume = grantVolume;
    }

    public BigDecimal getCurPoolVolume() {
        return curPoolVolume;
    }

    public void setCurPoolVolume(BigDecimal curPoolVolume) {
        this.curPoolVolume = curPoolVolume;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
