package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 挖矿规则
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_mining_conf")
public class Mk2PopularizeMiningConf {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("type")
    private String type;

    @SqlField("total_volume")
    private BigDecimal totalVolume;

    @SqlField("grant_volume")
    private BigDecimal grantVolume;

    @SqlField("show_grant_volume")
    private BigDecimal showGrantVolume;

    @SqlField("delay_show_multiple")
    private BigDecimal delayShowMultiple;

    @SqlField("per_volume")
    private BigDecimal perVolume;

    @SqlField("greater_volume")
    private BigDecimal greaterVolume;

    @SqlField("leader_greater_volume")
    private BigDecimal leaderGreaterVolume;    // 团队参于挖矿最小持有量

    @SqlField("base_volume")
    private BigDecimal baseVolume;

    @SqlField("income_hold_ratio")
    private BigDecimal incomeHoldRatio;

    @SqlField("base_multiple")
    private BigDecimal baseMultiple;

    @SqlField("coin_id")
    private String coinId;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    @SqlField("status")
    private String status;

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

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
    }

    public BigDecimal getGrantVolume() {
        return grantVolume;
    }

    public void setGrantVolume(BigDecimal grantVolume) {
        this.grantVolume = grantVolume;
    }

    public BigDecimal getPerVolume() {
        return perVolume;
    }

    public void setPerVolume(BigDecimal perVolume) {
        this.perVolume = perVolume;
    }

    public BigDecimal getGreaterVolume() {
        return greaterVolume;
    }

    public void setGreaterVolume(BigDecimal greaterVolume) {
        this.greaterVolume = greaterVolume;
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getBaseVolume() {
        return baseVolume;
    }

    public void setBaseVolume(BigDecimal baseVolume) {
        this.baseVolume = baseVolume;
    }

    public BigDecimal getIncomeHoldRatio() {
        return incomeHoldRatio;
    }

    public void setIncomeHoldRatio(BigDecimal incomeHoldRatio) {
        this.incomeHoldRatio = incomeHoldRatio;
    }

    public BigDecimal getBaseMultiple() {
        return baseMultiple;
    }

    public void setBaseMultiple(BigDecimal baseMultiple) {
        this.baseMultiple = baseMultiple;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getLeaderGreaterVolume() {
        return leaderGreaterVolume;
    }

    public void setLeaderGreaterVolume(BigDecimal leaderGreaterVolume) {
        this.leaderGreaterVolume = leaderGreaterVolume;
    }

    public BigDecimal getShowGrantVolume() {
        return showGrantVolume;
    }

    public void setShowGrantVolume(BigDecimal showGrantVolume) {
        this.showGrantVolume = showGrantVolume;
    }

    public BigDecimal getDelayShowMultiple() {
        return delayShowMultiple;
    }

    public void setDelayShowMultiple(BigDecimal delayShowMultiple) {
        this.delayShowMultiple = delayShowMultiple;
    }
}
