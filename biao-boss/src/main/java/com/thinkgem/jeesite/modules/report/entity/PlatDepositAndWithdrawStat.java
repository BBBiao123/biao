/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.Date;

/**
 * 平台充值提现统计Entity
 * @author zzj
 * @version 2018-12-21
 */
public class PlatDepositAndWithdrawStat extends DataEntity<PlatDepositAndWithdrawStat> {

	private static final long serialVersionUID = 1L;
	private String coinSymbol;		// 币种代号
	private Integer depositNumber;		// 币币
	private Double allocationFee;		// c2c资产
	private Double depositVolume;		// c2c资产
	private Double withdrawNumber;		// 冻结资产
	private Double withdrawApplyVolume;		// 总资产
	private Double withdrawVolume;		// 流水汇总
	private Double blockFeeVolume;		// 差额
	private Double withdrawFee;		// 拨币资产
	private Date beginCreateDate;	// 创建日期
	private Date endCreateDate;	// 创建日期

	public PlatDepositAndWithdrawStat() {
		super();
	}

	public PlatDepositAndWithdrawStat(String id){
		super(id);
	}

	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}

	public Integer getDepositNumber() {
		return depositNumber;
	}

	public void setDepositNumber(Integer depositNumber) {
		this.depositNumber = depositNumber;
	}

	public Double getDepositVolume() {
		return depositVolume;
	}

	public void setDepositVolume(Double depositVolume) {
		this.depositVolume = depositVolume;
	}

	public Double getAllocationFee() {
		return allocationFee;
	}

	public void setAllocationFee(Double allocationFee) {
		this.allocationFee = allocationFee;
	}

	public Double getWithdrawNumber() {
		return withdrawNumber;
	}

	public void setWithdrawNumber(Double withdrawNumber) {
		this.withdrawNumber = withdrawNumber;
	}

	public Double getWithdrawApplyVolume() {
		return withdrawApplyVolume;
	}

	public void setWithdrawApplyVolume(Double withdrawApplyVolume) {
		this.withdrawApplyVolume = withdrawApplyVolume;
	}

	public Double getWithdrawVolume() {
		return withdrawVolume;
	}

	public void setWithdrawVolume(Double withdrawVolume) {
		this.withdrawVolume = withdrawVolume;
	}

	public Double getBlockFeeVolume() {
		return blockFeeVolume;
	}

	public void setBlockFeeVolume(Double blockFeeVolume) {
		this.blockFeeVolume = blockFeeVolume;
	}

	public Double getWithdrawFee() {
		return withdrawFee;
	}

	public void setWithdrawFee(Double withdrawFee) {
		this.withdrawFee = withdrawFee;
	}

	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}

	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
}