/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;

import java.util.Date;

/**
 * ddEntity
 * @author zzj
 * @version 2018-10-10
 */
public class ReportPlatUserReconciliation extends DataEntity<ReportPlatUserReconciliation> {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String mail;
	private String mobile;
	private String coinSymbol;		// 币种
	private Double tradeVolume;		// 币币资产
	private Double offlineVolume;		// c2c资产
	private Double superVolume;		// 超级钱包资产
	private Double lockVolume;		// 冻结资产
	private Double totalVolume;		// 总资产
	private Double billSumVolume;		// 流水汇总
	private Double tradeRealVolume;		// 币币实际资产
	private Double balance;		// 余额

	public ReportPlatUserReconciliation() {
		super();
	}

	public ReportPlatUserReconciliation(String id){
		super(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}

	public Double getTradeVolume() {
		return tradeVolume;
	}

	public void setTradeVolume(Double tradeVolume) {
		this.tradeVolume = tradeVolume;
	}

	public Double getOfflineVolume() {
		return offlineVolume;
	}

	public void setOfflineVolume(Double offlineVolume) {
		this.offlineVolume = offlineVolume;
	}

	public Double getSuperVolume() {
		return superVolume;
	}

	public void setSuperVolume(Double superVolume) {
		this.superVolume = superVolume;
	}

	public Double getLockVolume() {
		return lockVolume;
	}

	public void setLockVolume(Double lockVolume) {
		this.lockVolume = lockVolume;
	}

	public Double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Double getBillSumVolume() {
		return billSumVolume;
	}

	public void setBillSumVolume(Double billSumVolume) {
		this.billSumVolume = billSumVolume;
	}

	public Double getTradeRealVolume() {
		return tradeRealVolume;
	}

	public void setTradeRealVolume(Double tradeRealVolume) {
		this.tradeRealVolume = tradeRealVolume;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
}