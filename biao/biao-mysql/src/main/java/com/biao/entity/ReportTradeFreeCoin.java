package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SqlTable("report_trade_fee_coin")
public class ReportTradeFreeCoin implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    private String id;

    @SqlField("coin")
    private String coin;

    @SqlField("sum_fee")
    private BigDecimal sumFee;

    @SqlField("count_time")
    private LocalDate countTime;

    @SqlField("create_time")
    private LocalDateTime createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public BigDecimal getSumFee() {
        return sumFee;
    }

    public void setSumFee(BigDecimal sumFee) {
        this.sumFee = sumFee;
    }

    public LocalDate getCountTime() {
        return countTime;
    }

    public void setCountTime(LocalDate countTime) {
        this.countTime = countTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

}
