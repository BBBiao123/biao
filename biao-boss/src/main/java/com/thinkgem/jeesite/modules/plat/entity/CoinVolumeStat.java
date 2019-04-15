/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * 币币资产Entity
 * @author dazi
 * @version 2018-04-27
 */
public class CoinVolumeStat extends DataEntity<CoinVolumeStat> {

	private static final long serialVersionUID = 1L;
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private BigDecimal volume;		// 可用资产
	private BigDecimal lockVolume;		// 冻结资产
	private BigDecimal totalVolume; //总资产
	private BigDecimal c2cVolume; //c2c总资产
	private BigDecimal depositVolume; //充值总资产
	private BigDecimal withdrawVolume; //提币总资产
	private BigDecimal balanceVolume; //差额

	public CoinVolumeStat() {
		super();
	}

	public CoinVolumeStat(String id){
		super(id);
	}

	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getLockVolume() {
		return lockVolume;
	}

	public void setLockVolume(BigDecimal lockVolume) {
		this.lockVolume = lockVolume;
	}

	public BigDecimal getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}

	public BigDecimal getC2cVolume() {
		return c2cVolume;
	}

	public void setC2cVolume(BigDecimal c2cVolume) {
		this.c2cVolume = c2cVolume;
	}

	public BigDecimal getDepositVolume() {
		return depositVolume;
	}

	public void setDepositVolume(BigDecimal depositVolume) {
		this.depositVolume = depositVolume;
	}

	public BigDecimal getBalanceVolume() {
		return balanceVolume;
	}

	public void setBalanceVolume(BigDecimal balanceVolume) {
		this.balanceVolume = balanceVolume;
	}

	public BigDecimal getWithdrawVolume() {
		return withdrawVolume;
	}

	public void setWithdrawVolume(BigDecimal withdrawVolume) {
		this.withdrawVolume = withdrawVolume;
	}
}