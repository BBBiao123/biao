package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

@SqlTable("mk_red_envelope_conf")
public class MkRedEnvelopeConf extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("name")
    private String name;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("single_lower_volume")
    private BigDecimal singleLowerVolume;
    @SqlField("single_higher_volume")
    private BigDecimal singleHigherVolume;

    @SqlField("lower_number")
    private Integer lowerNumber;
    @SqlField("higher_number")
    private Integer higherNumber;

    @SqlField("fee")
    private BigDecimal fee;
    @SqlField("destroy_user_id")
    private String destroyUserId;
    @SqlField("lucky_lower_volume")
    private BigDecimal luckyLowerVolume;
    @SqlField("lucky_higher_volume")
    private BigDecimal luckyHigherVolume;
    @SqlField("point_volume")
    private Integer pointVolume;
    @SqlField("status")
    private String status;
    @SqlField("remark")
    private String remark;

    @SqlField("version")
    private Long version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getSingleLowerVolume() {
        return singleLowerVolume;
    }

    public void setSingleLowerVolume(BigDecimal singleLowerVolume) {
        this.singleLowerVolume = singleLowerVolume;
    }

    public BigDecimal getSingleHigherVolume() {
        return singleHigherVolume;
    }

    public void setSingleHigherVolume(BigDecimal singleHigherVolume) {
        this.singleHigherVolume = singleHigherVolume;
    }

    public Integer getLowerNumber() {
        return lowerNumber;
    }

    public void setLowerNumber(Integer lowerNumber) {
        this.lowerNumber = lowerNumber;
    }

    public Integer getHigherNumber() {
        return higherNumber;
    }

    public void setHigherNumber(Integer higherNumber) {
        this.higherNumber = higherNumber;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getDestroyUserId() {
        return destroyUserId;
    }

    public void setDestroyUserId(String destroyUserId) {
        this.destroyUserId = destroyUserId;
    }

    public BigDecimal getLuckyLowerVolume() {
        return luckyLowerVolume;
    }

    public void setLuckyLowerVolume(BigDecimal luckyLowerVolume) {
        this.luckyLowerVolume = luckyLowerVolume;
    }

    public BigDecimal getLuckyHigherVolume() {
        return luckyHigherVolume;
    }

    public void setLuckyHigherVolume(BigDecimal luckyHigherVolume) {
        this.luckyHigherVolume = luckyHigherVolume;
    }

    public Integer getPointVolume() {
        return pointVolume;
    }

    public void setPointVolume(Integer pointVolume) {
        this.pointVolume = pointVolume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
