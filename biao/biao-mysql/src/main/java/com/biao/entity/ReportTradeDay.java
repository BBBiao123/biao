package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;
import java.time.LocalDate;

@SqlTable("report_trade_day")
public class ReportTradeDay extends BaseEntity {

    @SqlField("coin_main")
    private String coinMain;
    @SqlField("coin_other")
    private String coinOther;
    @SqlField("latest_price")
    private BigDecimal latestPrice;
    @SqlField("first_price")
    private BigDecimal firstPrice;
    @SqlField("highest_price")
    private BigDecimal highestPrice;
    @SqlField("lower_price")
    private BigDecimal lowerPrice;
    @SqlField("day_count")
    private BigDecimal dayCount;
    @SqlField("count_time")
    private LocalDate countTime;

    public String getCoinMain() {
        return coinMain;
    }

    public void setCoinMain(String coinMain) {
        this.coinMain = coinMain;
    }

    public String getCoinOther() {
        return coinOther;
    }

    public void setCoinOther(String coinOther) {
        this.coinOther = coinOther;
    }

    public BigDecimal getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(BigDecimal latestPrice) {
        this.latestPrice = latestPrice;
    }

    public BigDecimal getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(BigDecimal firstPrice) {
        this.firstPrice = firstPrice;
    }

    public BigDecimal getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(BigDecimal highestPrice) {
        this.highestPrice = highestPrice;
    }

    public BigDecimal getLowerPrice() {
        return lowerPrice;
    }

    public void setLowerPrice(BigDecimal lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    public BigDecimal getDayCount() {
        return dayCount;
    }

    public void setDayCount(BigDecimal dayCount) {
        this.dayCount = dayCount;
    }

    public LocalDate getCountTime() {
        return countTime;
    }

    public void setCountTime(LocalDate countTime) {
        this.countTime = countTime;
    }

}
