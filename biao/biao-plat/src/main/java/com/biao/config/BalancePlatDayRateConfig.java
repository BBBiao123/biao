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
}
