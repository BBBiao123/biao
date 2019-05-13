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
 * 用户余币宝资产
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


    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("refer_id")
    private String referId;

    @SqlField("yesterday_statics_income")
    private BigDecimal yesterdayStaticsIncome;

    @SqlField("yesterday_equality_reward")
    private BigDecimal yesterdayEqualityReward;

    @SqlField("yesterday_dynamics_income")
    private BigDecimal yesterdayDynamicsIncome;

    @SqlField("yesterday_community_reward")
    private BigDecimal yesterdayCommunityReward;

    @SqlField("team_level")
    private String  teamLevel;

    @SqlField("team_amount")
    private BigDecimal teamAmount;

    @SqlField("team_community_amount")
    private BigDecimal teamCommunityAmount;

    @SqlField("sum_revenue")
    private BigDecimal sumRevenue;

    @SqlField("yesterday_revenue")
    private BigDecimal yesterdayRevenue;

    @SqlField("valid_num")
    private int validNum;

    @SqlField("mobile")
    private String mobile;

    @SqlField("mail")
    private String mail;

    private int ordNum;

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

    public BigDecimal getYesterdayStaticsIncome() {
        return yesterdayStaticsIncome;
    }

    public void setYesterdayStaticsIncome(BigDecimal yesterdayStaticsIncome) {
        this.yesterdayStaticsIncome = yesterdayStaticsIncome;
    }

    public BigDecimal getYesterdayEqualityReward() {
        return yesterdayEqualityReward;
    }

    public void setYesterdayEqualityReward(BigDecimal yesterdayEqualityReward) {
        this.yesterdayEqualityReward = yesterdayEqualityReward;
    }

    public BigDecimal getYesterdayDynamicsIncome() {
        return yesterdayDynamicsIncome;
    }

    public void setYesterdayDynamicsIncome(BigDecimal yesterdayDynamicsIncome) {
        this.yesterdayDynamicsIncome = yesterdayDynamicsIncome;
    }

    public BigDecimal getYesterdayCommunityReward() {
        return yesterdayCommunityReward;
    }

    public void setYesterdayCommunityReward(BigDecimal yesterdayCommunityReward) {
        this.yesterdayCommunityReward = yesterdayCommunityReward;
    }

    public String getTeamLevel() {
        return teamLevel;
    }

    public void setTeamLevel(String teamLevel) {
        this.teamLevel = teamLevel;
    }

    public BigDecimal getTeamAmount() {
        return teamAmount;
    }

    public void setTeamAmount(BigDecimal teamAmount) {
        this.teamAmount = teamAmount;
    }

    public BigDecimal getTeamCommunityAmount() {
        return teamCommunityAmount;
    }

    public void setTeamCommunityAmount(BigDecimal teamCommunityAmount) {
        this.teamCommunityAmount = teamCommunityAmount;
    }

    public BigDecimal getSumRevenue() {
        return sumRevenue;
    }

    public void setSumRevenue(BigDecimal sumRevenue) {
        this.sumRevenue = sumRevenue;
    }

    public BigDecimal getYesterdayRevenue() {
        return yesterdayRevenue;
    }

    public void setYesterdayRevenue(BigDecimal yesterdayRevenue) {
        this.yesterdayRevenue = yesterdayRevenue;
    }

    public int getValidNum() {
        return validNum;
    }

    public void setValidNum(int validNum) {
        this.validNum = validNum;
    }

    public int getOrdNum() {
        return ordNum;
    }

    public void setOrdNum(int ordNum) {
        this.ordNum = ordNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
