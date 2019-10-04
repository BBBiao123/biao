package com.biao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "balance")
public class BalancePlatDayRateConfig {

	private BigDecimal oneDayRate ;
	private BigDecimal secondDayRate ;
	private BigDecimal threeDayRate ;
	private BigDecimal equalReward ;
	private String rewardDateStr ;
	private String coinSmybol;
	private BigDecimal rewardNum;
	private String coinLockSmybol;
	private BigDecimal minLockCoinNum;

	public BigDecimal getOneDayRate() {
		return oneDayRate;
	}

	public void setOneDayRate(BigDecimal oneDayRate) {
		this.oneDayRate = oneDayRate;
	}

	public BigDecimal getSecondDayRate() {
		return secondDayRate;
	}

	public void setSecondDayRate(BigDecimal secondDayRate) {
		this.secondDayRate = secondDayRate;
	}

	public BigDecimal getThreeDayRate() {
		return threeDayRate;
	}

	public void setThreeDayRate(BigDecimal threeDayRate) {
		this.threeDayRate = threeDayRate;
	}

	public BigDecimal getEqualReward() {
		return equalReward;
	}

	public void setEqualReward(BigDecimal equalReward) {
		this.equalReward = equalReward;
	}

	public String getRewardDateStr() {
		return rewardDateStr;
	}

	public void setRewardDateStr(String rewardDateStr) {
		this.rewardDateStr = rewardDateStr;
	}

	public String getCoinSmybol() {
		return coinSmybol;
	}

	public void setCoinSmybol(String coinSmybol) {
		this.coinSmybol = coinSmybol;
	}

	public BigDecimal getRewardNum() {
		return rewardNum;
	}

	public void setRewardNum(BigDecimal rewardNum) {
		this.rewardNum = rewardNum;
	}

	public String getCoinLockSmybol() {
		return coinLockSmybol;
	}

	public void setCoinLockSmybol(String coinLockSmybol) {
		this.coinLockSmybol = coinLockSmybol;
	}

	public BigDecimal getMinLockCoinNum() {
		return minLockCoinNum;
	}

	public void setMinLockCoinNum(BigDecimal minLockCoinNum) {
		this.minLockCoinNum = minLockCoinNum;
	}
}
