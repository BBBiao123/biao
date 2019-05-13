package com.biao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "balance")
public class BalancePlatDayRateConfig {

	private BigDecimal oneDayRate ;
	private BigDecimal secondDayRate ;
	private BigDecimal equalReward ;

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

	public BigDecimal getEqualReward() {
		return equalReward;
	}

	public void setEqualReward(BigDecimal equalReward) {
		this.equalReward = equalReward;
	}
}