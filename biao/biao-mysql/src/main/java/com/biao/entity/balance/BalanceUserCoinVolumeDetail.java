package com.biao.entity.balance;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户资产
 *
 *  ""
 */
@SqlTable("js_plat_user_coin_incomedetail")
public class BalanceUserCoinVolumeDetail extends BaseEntity {

    @SqlField("user_id")
    private String userId;


    @SqlField("income_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime incomeDate;

    @SqlField("detail_income")
    private BigDecimal detailIncome;

    @SqlField("detail_reward")
    private BigDecimal detailReward;




    @SqlField("version")
    private int version;

    @SqlField("coin_symbol")
    private String coinSymbol;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public LocalDateTime getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(LocalDateTime incomeDate) {
        this.incomeDate = incomeDate;
    }

    public BigDecimal getDetailIncome() {
        return detailIncome;
    }

    public void setDetailIncome(BigDecimal detailIncome) {
        this.detailIncome = detailIncome;
    }

    public BigDecimal getDetailReward() {
        return detailReward;
    }

    public void setDetailReward(BigDecimal detailReward) {
        this.detailReward = detailReward;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
