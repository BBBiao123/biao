package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeDeserializer;
import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Mk2UserStatisticsInfo implements Serializable {

    private String userId;
    private String coinSymbol;

    // 挖矿总览
    private BigDecimal grantMiningCoinVolumeTotal;// 已挖矿量
    private BigDecimal miningCoinVolumeTotal;  //  可挖总量
    private BigDecimal lastMiningCoinVolumeTotal;   // 昨日挖矿总量 (持币挖矿 + 团队挖矿)
    private BigDecimal lastMingHoldCoinTotal;   // 昨日持币挖矿
    private BigDecimal lastMingTeamCoinTotal;   // 昨日团队挖矿
    private BigDecimal lockCoinVolumeTotal;     // 锁仓总量
    private BigDecimal destroyCoinVolumeTotal;  // 销毁总量
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime lastMiningDate;   // 上次挖矿时间
    private BigDecimal lastBestHoldCoinVolume;  // 上次最佳持币量

    // 我的收益
    private BigDecimal myLastBonusVolume;   // 昨日分红
    private BigDecimal myLastMiningVolume;  // 昨日挖矿总量
    private BigDecimal myLastMiningHoldVolume;  // 昨日持币挖矿
    private BigDecimal myLastMiningTeamVolume;  // 昨日团队挖矿
    private BigDecimal myHoldTotalMiningVolume; // 我的持币挖矿总量
    private BigDecimal myTeamTotalMiningVolume; // 我的团队挖矿总量

    // 持币挖矿流水
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime holdMiningDate;   // 持币挖矿日期
    private BigDecimal holdMiningVolume;    // 持币数量
    private Integer holdMiningOrderNo;      // 持币排名
    private BigDecimal holdMiningGiveVolume;    // 挖矿量

    // 团队挖矿流水
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime teamMiningDate;   // 团队挖矿日期
    private long teamMiningPersonNumber;    // 团队人数
    private BigDecimal teamMiningVolume;    // 团队参与挖矿持币量
    private BigDecimal teamMiningAreaVolume;    // 大区持币总量
    private BigDecimal teamMiningGiveVolume;    // 挖矿量

    // 区域合伙人分红
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime areaBonusDate;    // 区域分红日期
    private BigDecimal areaBonusFixedVolume;    // 区域分红固定分红
    private BigDecimal areaBonusPhoneVolume;    // 区域分红手机归属地分红
    private BigDecimal areaBonusReferVolume;     // 区域分红团队手续费分红

    // 节点人分红
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime nodalBonusDate;   // 节点人分红日期
    private BigDecimal nodalBonusVolume;    // 节点人分红量

    // 普通会员分红
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime commonBonusDate;   // 普通会员分红日期
    private BigDecimal commonBonusVolume;    // 普通会员分红量

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public BigDecimal getGrantMiningCoinVolumeTotal() {
        return grantMiningCoinVolumeTotal;
    }

    public void setGrantMiningCoinVolumeTotal(BigDecimal grantMiningCoinVolumeTotal) {
        this.grantMiningCoinVolumeTotal = grantMiningCoinVolumeTotal;
    }

    public BigDecimal getMiningCoinVolumeTotal() {
        return miningCoinVolumeTotal;
    }

    public void setMiningCoinVolumeTotal(BigDecimal miningCoinVolumeTotal) {
        this.miningCoinVolumeTotal = miningCoinVolumeTotal;
    }

    public BigDecimal getLastMiningCoinVolumeTotal() {
        return lastMiningCoinVolumeTotal;
    }

    public void setLastMiningCoinVolumeTotal(BigDecimal lastMiningCoinVolumeTotal) {
        this.lastMiningCoinVolumeTotal = lastMiningCoinVolumeTotal;
    }

    public BigDecimal getLockCoinVolumeTotal() {
        return lockCoinVolumeTotal;
    }

    public void setLockCoinVolumeTotal(BigDecimal lockCoinVolumeTotal) {
        this.lockCoinVolumeTotal = lockCoinVolumeTotal;
    }

    public BigDecimal getDestroyCoinVolumeTotal() {
        return destroyCoinVolumeTotal;
    }

    public void setDestroyCoinVolumeTotal(BigDecimal destroyCoinVolumeTotal) {
        this.destroyCoinVolumeTotal = destroyCoinVolumeTotal;
    }

    public BigDecimal getMyLastBonusVolume() {
        return myLastBonusVolume;
    }

    public void setMyLastBonusVolume(BigDecimal myLastBonusVolume) {
        this.myLastBonusVolume = myLastBonusVolume;
    }

    public BigDecimal getMyLastMiningVolume() {
        return myLastMiningVolume;
    }

    public void setMyLastMiningVolume(BigDecimal myLastMiningVolume) {
        this.myLastMiningVolume = myLastMiningVolume;
    }

    public BigDecimal getMyLastMiningHoldVolume() {
        return myLastMiningHoldVolume;
    }

    public void setMyLastMiningHoldVolume(BigDecimal myLastMiningHoldVolume) {
        this.myLastMiningHoldVolume = myLastMiningHoldVolume;
    }

    public BigDecimal getMyLastMiningTeamVolume() {
        return myLastMiningTeamVolume;
    }

    public void setMyLastMiningTeamVolume(BigDecimal myLastMiningTeamVolume) {
        this.myLastMiningTeamVolume = myLastMiningTeamVolume;
    }

    public LocalDateTime getHoldMiningDate() {
        return holdMiningDate;
    }

    public void setHoldMiningDate(LocalDateTime holdMiningDate) {
        this.holdMiningDate = holdMiningDate;
    }

    public BigDecimal getHoldMiningVolume() {
        return holdMiningVolume;
    }

    public void setHoldMiningVolume(BigDecimal holdMiningVolume) {
        this.holdMiningVolume = holdMiningVolume;
    }

    public Integer getHoldMiningOrderNo() {
        return holdMiningOrderNo;
    }

    public void setHoldMiningOrderNo(Integer holdMiningOrderNo) {
        this.holdMiningOrderNo = holdMiningOrderNo;
    }

    public BigDecimal getHoldMiningGiveVolume() {
        return holdMiningGiveVolume;
    }

    public void setHoldMiningGiveVolume(BigDecimal holdMiningGiveVolume) {
        this.holdMiningGiveVolume = holdMiningGiveVolume;
    }

    public LocalDateTime getTeamMiningDate() {
        return teamMiningDate;
    }

    public void setTeamMiningDate(LocalDateTime teamMiningDate) {
        this.teamMiningDate = teamMiningDate;
    }

    public long getTeamMiningPersonNumber() {
        return teamMiningPersonNumber;
    }

    public void setTeamMiningPersonNumber(long teamMiningPersonNumber) {
        this.teamMiningPersonNumber = teamMiningPersonNumber;
    }

    public BigDecimal getTeamMiningVolume() {
        return teamMiningVolume;
    }

    public void setTeamMiningVolume(BigDecimal teamMiningVolume) {
        this.teamMiningVolume = teamMiningVolume;
    }

    public BigDecimal getTeamMiningAreaVolume() {
        return teamMiningAreaVolume;
    }

    public void setTeamMiningAreaVolume(BigDecimal teamMiningAreaVolume) {
        this.teamMiningAreaVolume = teamMiningAreaVolume;
    }

    public BigDecimal getTeamMiningGiveVolume() {
        return teamMiningGiveVolume;
    }

    public void setTeamMiningGiveVolume(BigDecimal teamMiningGiveVolume) {
        this.teamMiningGiveVolume = teamMiningGiveVolume;
    }

    public LocalDateTime getAreaBonusDate() {
        return areaBonusDate;
    }

    public void setAreaBonusDate(LocalDateTime areaBonusDate) {
        this.areaBonusDate = areaBonusDate;
    }

    public BigDecimal getAreaBonusFixedVolume() {
        return areaBonusFixedVolume;
    }

    public void setAreaBonusFixedVolume(BigDecimal areaBonusFixedVolume) {
        this.areaBonusFixedVolume = areaBonusFixedVolume;
    }

    public BigDecimal getAreaBonusPhoneVolume() {
        return areaBonusPhoneVolume;
    }

    public void setAreaBonusPhoneVolume(BigDecimal areaBonusPhoneVolume) {
        this.areaBonusPhoneVolume = areaBonusPhoneVolume;
    }

    public BigDecimal getAreaBonusReferVolume() {
        return areaBonusReferVolume;
    }

    public void setAreaBonusReferVolume(BigDecimal areaBonusReferVolume) {
        this.areaBonusReferVolume = areaBonusReferVolume;
    }

    public LocalDateTime getNodalBonusDate() {
        return nodalBonusDate;
    }

    public void setNodalBonusDate(LocalDateTime nodalBonusDate) {
        this.nodalBonusDate = nodalBonusDate;
    }

    public BigDecimal getNodalBonusVolume() {
        return nodalBonusVolume;
    }

    public void setNodalBonusVolume(BigDecimal nodalBonusVolume) {
        this.nodalBonusVolume = nodalBonusVolume;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCommonBonusDate() {
        return commonBonusDate;
    }

    public void setCommonBonusDate(LocalDateTime commonBonusDate) {
        this.commonBonusDate = commonBonusDate;
    }

    public BigDecimal getCommonBonusVolume() {
        return commonBonusVolume;
    }

    public void setCommonBonusVolume(BigDecimal commonBonusVolume) {
        this.commonBonusVolume = commonBonusVolume;
    }

    public LocalDateTime getLastMiningDate() {
        return lastMiningDate;
    }

    public void setLastMiningDate(LocalDateTime lastMiningDate) {
        this.lastMiningDate = lastMiningDate;
    }

    public BigDecimal getLastBestHoldCoinVolume() {
        return lastBestHoldCoinVolume;
    }

    public void setLastBestHoldCoinVolume(BigDecimal lastBestHoldCoinVolume) {
        this.lastBestHoldCoinVolume = lastBestHoldCoinVolume;
    }

    public BigDecimal getLastMingHoldCoinTotal() {
        return lastMingHoldCoinTotal;
    }

    public void setLastMingHoldCoinTotal(BigDecimal lastMingHoldCoinTotal) {
        this.lastMingHoldCoinTotal = lastMingHoldCoinTotal;
    }

    public BigDecimal getLastMingTeamCoinTotal() {
        return lastMingTeamCoinTotal;
    }

    public void setLastMingTeamCoinTotal(BigDecimal lastMingTeamCoinTotal) {
        this.lastMingTeamCoinTotal = lastMingTeamCoinTotal;
    }

    public BigDecimal getMyHoldTotalMiningVolume() {
        return myHoldTotalMiningVolume;
    }

    public void setMyHoldTotalMiningVolume(BigDecimal myHoldTotalMiningVolume) {
        this.myHoldTotalMiningVolume = myHoldTotalMiningVolume;
    }

    public BigDecimal getMyTeamTotalMiningVolume() {
        return myTeamTotalMiningVolume;
    }

    public void setMyTeamTotalMiningVolume(BigDecimal myTeamTotalMiningVolume) {
        this.myTeamTotalMiningVolume = myTeamTotalMiningVolume;
    }
}
