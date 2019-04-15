package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SqlTable("report_trade_fee_record")
public class ReportTradeFreeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    private String id;

    @SqlField("coin_main")
    private String coinMain;

    @SqlField("coin_other")
    private String coinOther;

    @SqlField("main_free")
    private BigDecimal mainFree;

    @SqlField("other_free")
    private BigDecimal otherFree;

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

    public BigDecimal getMainFree() {
        return mainFree;
    }

    public void setMainFree(BigDecimal mainFree) {
        this.mainFree = mainFree;
    }

    public BigDecimal getOtherFree() {
        return otherFree;
    }

    public void setOtherFree(BigDecimal otherFree) {
        this.otherFree = otherFree;
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
