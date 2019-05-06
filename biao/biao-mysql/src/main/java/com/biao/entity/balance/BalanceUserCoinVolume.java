package com.biao.entity.balance;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.biao.vo.UserCoinVolumeVO;
import com.biao.vo.redis.RedisUserCoinVolume;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户资产
 *
 *  ""
 */
@SqlTable("js_plat_user_coin_balance")
public class BalanceUserCoinVolume extends BaseEntity {

    @SqlField("user_id")
    private String userId;

    @SqlField("coin_balance")
    private BigDecimal coinBalance;

    @SqlField("day_rate")
    private BigDecimal dayRate;

    @SqlField("accumul_income")
    private BigDecimal accumulIncome;

    @SqlField("yesterday_income")
    private BigDecimal yesterdayIncome;


    @SqlField("accumul_reward")
    private BigDecimal accumulReward;

    @SqlField("yesterday_reward")
    private BigDecimal yesterdayReward;

//
//    @SqlField("coin_id")
//    private String coinId;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("refer_id")
    private String referId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getCoinBalance() {
        return coinBalance;
    }

    public void setCoinBalance(BigDecimal coinBalance) {
        this.coinBalance = coinBalance;
    }

    public BigDecimal getDayRate() {
        return dayRate;
    }

    public void setDayRate(BigDecimal dayRate) {
        this.dayRate = dayRate;
    }

    public BigDecimal getAccumulIncome() {
        return accumulIncome;
    }

    public void setAccumulIncome(BigDecimal accumulIncome) {
        this.accumulIncome = accumulIncome;
    }

    public BigDecimal getYesterdayIncome() {
        return yesterdayIncome;
    }

    public void setYesterdayIncome(BigDecimal yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
    }

    public BigDecimal getAccumulReward() {
        return accumulReward;
    }

    public void setAccumulReward(BigDecimal accumulReward) {
        this.accumulReward = accumulReward;
    }

    public BigDecimal getYesterdayReward() {
        return yesterdayReward;
    }

    public void setYesterdayReward(BigDecimal yesterdayReward) {
        this.yesterdayReward = yesterdayReward;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }
}
