package com.biao.entity.mkcommon;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("mk_common_coin_rate")
public class MkCommonCoinRate {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("main_coin_id")
    private String mainCoinId;

    @SqlField("main_coin_symbol")
    private String mainCoinSymbol;

    @SqlField("other_coin_id")
    private String otherCoinId;

    @SqlField("other_coin_symbol")
    private String otherCoinSymbol;

    @SqlField("rate")
    private BigDecimal rate;

    @SqlField("begin_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime beginDate;

    @SqlField("end_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endDate;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime create_date;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainCoinId() {
        return mainCoinId;
    }

    public void setMainCoinId(String mainCoinId) {
        this.mainCoinId = mainCoinId;
    }

    public String getMainCoinSymbol() {
        return mainCoinSymbol;
    }

    public void setMainCoinSymbol(String mainCoinSymbol) {
        this.mainCoinSymbol = mainCoinSymbol;
    }

    public String getOtherCoinId() {
        return otherCoinId;
    }

    public void setOtherCoinId(String otherCoinId) {
        this.otherCoinId = otherCoinId;
    }

    public String getOtherCoinSymbol() {
        return otherCoinSymbol;
    }

    public void setOtherCoinSymbol(String otherCoinSymbol) {
        this.otherCoinSymbol = otherCoinSymbol;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
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

    public LocalDateTime getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDateTime create_date) {
        this.create_date = create_date;
    }
}
