package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

@SqlTable("js_plat_offline_coin_task_record")
public class OfflineCoinTaskRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("status")
    private String status;
    @SqlField("symbol")
    private String symbol;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("before_max_price")
    private BigDecimal beforeMaxPrice;
    @SqlField("before_min_price")
    private BigDecimal beforeMinPrice;
    @SqlField("day_inc_price")
    private BigDecimal dayIncPrice;
    @SqlField("remark")
    private String remark;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public BigDecimal getBeforeMaxPrice() {
        return beforeMaxPrice;
    }

    public void setBeforeMaxPrice(BigDecimal beforeMaxPrice) {
        this.beforeMaxPrice = beforeMaxPrice;
    }

    public BigDecimal getBeforeMinPrice() {
        return beforeMinPrice;
    }

    public void setBeforeMinPrice(BigDecimal beforeMinPrice) {
        this.beforeMinPrice = beforeMinPrice;
    }

    public BigDecimal getDayIncPrice() {
        return dayIncPrice;
    }

    public void setDayIncPrice(BigDecimal dayIncPrice) {
        this.dayIncPrice = dayIncPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
