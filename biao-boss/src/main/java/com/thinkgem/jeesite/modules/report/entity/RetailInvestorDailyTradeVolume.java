/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 操盘手资产快照Entity
 * @author zzj
 * @version 2018-12-21
 */
public class RetailInvestorDailyTradeVolume extends DataEntity<RetailInvestorDailyTradeVolume> {

	private static final long serialVersionUID = 1L;
	private Date tradeDate;		// 交易日期
	private String userTag;		// 用户类型
	private String exType;		// 操作类型
	private String coinSymbol;		// 买入币种
	private Double spentVolume;		// 成交数量
	private String toCoinSymbol;		// 被交易币种
	private Double earnVolume;		// 得到的币种数量
	private String feeCoinSymbol;		// 手续费币种
	private Double feeVolume;		// 手续费数量
	private String exPairId;


	public RetailInvestorDailyTradeVolume() {
		super();
	}

	public RetailInvestorDailyTradeVolume(String id){
		super(id);
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getUserTag() {
		return userTag;
	}

	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}

	public String getExType() {
		return exType;
	}

	public void setExType(String exType) {
		this.exType = exType;
	}

	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}

	public Double getSpentVolume() {
		return spentVolume;
	}

	public void setSpentVolume(Double spentVolume) {
		this.spentVolume = spentVolume;
	}

	public String getToCoinSymbol() {
		return toCoinSymbol;
	}

	public void setToCoinSymbol(String toCoinSymbol) {
		this.toCoinSymbol = toCoinSymbol;
	}

	public Double getEarnVolume() {
		return earnVolume;
	}

	public void setEarnVolume(Double earnVolume) {
		this.earnVolume = earnVolume;
	}

	public String getFeeCoinSymbol() {
		return feeCoinSymbol;
	}

	public void setFeeCoinSymbol(String feeCoinSymbol) {
		this.feeCoinSymbol = feeCoinSymbol;
	}

	public Double getFeeVolume() {
		return feeVolume;
	}

	public void setFeeVolume(Double feeVolume) {
		this.feeVolume = feeVolume;
	}

	public String getExPairId() {
		return exPairId;
	}

	public void setExPairId(String exPairId) {
		this.exPairId = exPairId;
	}
}