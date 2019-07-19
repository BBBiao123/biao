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

    @SqlField("statics_income")
    private BigDecimal staticsIncome;

    @SqlField("team_record")
    private BigDecimal teamRecord;

    @SqlField("team_community_record")
    private BigDecimal teamCommunityRecord;

    @SqlField("team_level")
    private int teamLevel;

    @SqlField("community_statics_income")
    private BigDecimal communityStaticsIncome;

    @SqlField("node_number")
    private int nodeNumber;

    @SqlField("reality_statics_income")
    private BigDecimal realityStaticsIncome;

    @SqlField("community_manage_reward")
    private BigDecimal communityManageReward;

    @SqlField("dynamics_income")
    private BigDecimal dynamicsIncome;

    @SqlField("equality_reward")
    private BigDecimal equalityReward;

    @SqlField("sum_revenue")
    private BigDecimal sumRevenue;

    @SqlField("refer_id")
    private String referId;

    @SqlField("valid_num")
    private int validNum;

    @SqlField("community_sum_manage_reward")
    private BigDecimal communitySumManageReward;

    @SqlField("team_coin_record")
    private BigDecimal teamCoinRecord;

    @SqlField("level_difference_reward")
    private BigDecimal levelDifferenceReward;

    @SqlField("one_level_income")
    private BigDecimal oneLevelIncome;


    public BigDecimal getDynamicsIncome() {
        return dynamicsIncome;
    }

    public void setDynamicsIncome(BigDecimal dynamicsIncome) {
        this.dynamicsIncome = dynamicsIncome;
    }

    @SqlField("version")
    private int version;

    public BigDecimal getTeamRecord() {
        return teamRecord;
    }

    public void setTeamRecord(BigDecimal teamRecord) {
        this.teamRecord = teamRecord;
    }

    public BigDecimal getTeamCommunityRecord() {
        return teamCommunityRecord;
    }

    public void setTeamCommunityRecord(BigDecimal teamCommunityRecord) {
        this.teamCommunityRecord = teamCommunityRecord;
    }

    public int getTeamLevel() {
        return teamLevel;
    }

    public void setTeamLevel(int teamLevel) {
        this.teamLevel = teamLevel;
    }

    public BigDecimal getCommunityStaticsIncome() {
        return communityStaticsIncome;
    }

    public void setCommunityStaticsIncome(BigDecimal communityStaticsIncome) {
        this.communityStaticsIncome = communityStaticsIncome;
    }

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

    public BigDecimal getStaticsIncome() {
        return staticsIncome;
    }

    public void setStaticsIncome(BigDecimal staticsIncome) {
        this.staticsIncome = staticsIncome;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }
    public BigDecimal getRealityStaticsIncome() {
        return realityStaticsIncome;
    }

    public void setRealityStaticsIncome(BigDecimal realityStaticsIncome) {
        this.realityStaticsIncome = realityStaticsIncome;
    }

    public BigDecimal getCommunityManageReward() {
        return communityManageReward;
    }

    public void setCommunityManageReward(BigDecimal communityManageReward) {
        this.communityManageReward = communityManageReward;
    }

    public BigDecimal getEqualityReward() {
        return equalityReward;
    }

    public void setEqualityReward(BigDecimal equalityReward) {
        this.equalityReward = equalityReward;
    }

    public BigDecimal getSumRevenue() {
        return sumRevenue;
    }

    public void setSumRevenue(BigDecimal sumRevenue) {
        this.sumRevenue = sumRevenue;
    }

    public int getValidNum() {
        return validNum;
    }

    public void setValidNum(int validNum) {
        this.validNum = validNum;
    }



    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }

    public BigDecimal getCommunitySumManageReward() {
        return communitySumManageReward;
    }

    public void setCommunitySumManageReward(BigDecimal communitySumManageReward) {
        this.communitySumManageReward = communitySumManageReward;
    }

    public BigDecimal getTeamCoinRecord() {
        return teamCoinRecord;
    }

    public void setTeamCoinRecord(BigDecimal teamCoinRecord) {
        this.teamCoinRecord = teamCoinRecord;
    }

    public BigDecimal getLevelDifferenceReward() {
        return levelDifferenceReward;
    }

    public void setLevelDifferenceReward(BigDecimal levelDifferenceReward) {
        this.levelDifferenceReward = levelDifferenceReward;
    }

    public BigDecimal getOneLevelIncome() {
        return oneLevelIncome;
    }

    public void setOneLevelIncome(BigDecimal oneLevelIncome) {
        this.oneLevelIncome = oneLevelIncome;
    }


}
