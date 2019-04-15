package com.biao.entity.lucky;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

@SqlTable("mk_lucky_draw_config")
public class MkLuckyDrawConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("name")
    private String name;
    @SqlField("periods")
    private Integer periods;
    @SqlField("status")
    private String status;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("start_volume")
    private BigDecimal startVolume;
    @SqlField("step_add_volume")
    private BigDecimal stepAddVolume;
    @SqlField("min_volume")
    private BigDecimal minVolume;
    @SqlField("grant_volume")
    private BigDecimal grantVolume;
    @SqlField("pool_volume")
    private BigDecimal poolVolume;
    @SqlField("deduct_fee")
    private BigDecimal deductFee;
    @SqlField("fee")
    private BigDecimal fee;
    @SqlField("player_number")
    private Integer playerNumber;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("remark")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public BigDecimal getPoolVolume() {
        return poolVolume;
    }

    public void setPoolVolume(BigDecimal poolVolume) {
        this.poolVolume = poolVolume;
    }

    public BigDecimal getDeductFee() {
        return deductFee;
    }

    public void setDeductFee(BigDecimal deductFee) {
        this.deductFee = deductFee;
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

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
