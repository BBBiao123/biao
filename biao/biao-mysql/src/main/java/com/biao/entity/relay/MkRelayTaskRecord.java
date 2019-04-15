package com.biao.entity.relay;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("mk_relay_task_record")
public class MkRelayTaskRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("type")
    private String type;
    @SqlField("status")
    private String status;
    @SqlField("begin_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime beginDate;
    @SqlField("end_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endDate;
    @SqlField("increase_number")
    private Integer increaseNumber;
    @SqlField("increase_volume")
    private BigDecimal increaseVolume;
    @SqlField("pool_volume")
    private BigDecimal poolVolume;
    @SqlField("prize_number")
    private Integer prizeNumber;
    @SqlField("prize_volume")
    private BigDecimal prizeVolume;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("remark")
    private String remark;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public Integer getIncreaseNumber() {
        return increaseNumber;
    }

    public void setIncreaseNumber(Integer increaseNumber) {
        this.increaseNumber = increaseNumber;
    }

    public BigDecimal getIncreaseVolume() {
        return increaseVolume;
    }

    public void setIncreaseVolume(BigDecimal increaseVolume) {
        this.increaseVolume = increaseVolume;
    }

    public BigDecimal getPoolVolume() {
        return poolVolume;
    }

    public void setPoolVolume(BigDecimal poolVolume) {
        this.poolVolume = poolVolume;
    }

    public Integer getPrizeNumber() {
        return prizeNumber;
    }

    public void setPrizeNumber(Integer prizeNumber) {
        this.prizeNumber = prizeNumber;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
