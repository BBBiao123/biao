package com.biao.entity.balance;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户收益和奖励明细
 *
 *  ""
 */
@SqlTable("js_plat_jackpot_income")
public class BalancePlatJackpotVolumeDetail extends BaseEntity {

    @SqlField("coin_symbol")
    private String coinSymbol;


    @SqlField("reward_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime rewardDate;

    @SqlField("all_coin_income")
    private BigDecimal allCoinIncome;

    private long rewardTime;

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public LocalDateTime getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(LocalDateTime rewardDate) {
        this.rewardDate = rewardDate;
    }

    public BigDecimal getAllCoinIncome() {
        return allCoinIncome;
    }

    public void setAllCoinIncome(BigDecimal allCoinIncome) {
        this.allCoinIncome = allCoinIncome;
    }

    public long getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(long rewardTime) {
        this.rewardTime = rewardTime;
    }
}
