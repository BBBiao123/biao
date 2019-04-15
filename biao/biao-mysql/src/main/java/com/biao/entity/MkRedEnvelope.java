package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

@SqlTable("mk_red_envelope")
public class MkRedEnvelope extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("mobile")
    private String mobile;
    @SqlField("mail")
    private String mail;
    @SqlField("real_name")
    private String realName;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("type")
    private String type;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("receive_volume")
    private BigDecimal receiveVolume;
    @SqlField("total_number")
    private Integer totalNumber;
    @SqlField("receive_number")
    private Integer receiveNumber;
    @SqlField("status")
    private String status;
    @SqlField("best_with")
    private String bestWith;
    @SqlField("remark")
    private String remark;

    @SqlField("version")
    private Long version;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getReceiveVolume() {
        return receiveVolume;
    }

    public void setReceiveVolume(BigDecimal receiveVolume) {
        this.receiveVolume = receiveVolume;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getReceiveNumber() {
        return receiveNumber;
    }

    public void setReceiveNumber(Integer receiveNumber) {
        this.receiveNumber = receiveNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBestWith() {
        return bestWith;
    }

    public void setBestWith(String bestWith) {
        this.bestWith = bestWith;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
