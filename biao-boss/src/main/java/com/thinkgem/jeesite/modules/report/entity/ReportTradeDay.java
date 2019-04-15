/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 主区各币种交易量Entity
 * @author dazi
 * @version 2018-05-12
 */
public class ReportTradeDay extends DataEntity<ReportTradeDay> {
	
	private static final long serialVersionUID = 1L;
	private String coinMain;		// 主区
	private String coinOther;		// 被交易区
	private String latestPrice;		// 收盘价
	private String firstPrice;		// 开盘价
	private String highestPrice;		// 最高价
	private String lowerPrice;		// 最低价
	private String dayCount;		// 交易量
	private Date countTime;		// 统计时间
	
	public ReportTradeDay() {
		super();
	}

	public ReportTradeDay(String id){
		super(id);
	}

	@Length(min=0, max=10, message="主区长度必须介于 0 和 10 之间")
	public String getCoinMain() {
		return coinMain;
	}

	public void setCoinMain(String coinMain) {
		this.coinMain = coinMain;
	}
	
	@Length(min=0, max=10, message="被交易区长度必须介于 0 和 10 之间")
	public String getCoinOther() {
		return coinOther;
	}

	public void setCoinOther(String coinOther) {
		this.coinOther = coinOther;
	}
	
	public String getLatestPrice() {
		return latestPrice;
	}

	public void setLatestPrice(String latestPrice) {
		this.latestPrice = latestPrice;
	}
	
	public String getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(String firstPrice) {
		this.firstPrice = firstPrice;
	}
	
	public String getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(String highestPrice) {
		this.highestPrice = highestPrice;
	}
	
	public String getLowerPrice() {
		return lowerPrice;
	}

	public void setLowerPrice(String lowerPrice) {
		this.lowerPrice = lowerPrice;
	}
	
	public String getDayCount() {
		return dayCount;
	}

	public void setDayCount(String dayCount) {
		this.dayCount = dayCount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCountTime() {
		return countTime;
	}

	public void setCountTime(Date countTime) {
		this.countTime = countTime;
	}
	
}