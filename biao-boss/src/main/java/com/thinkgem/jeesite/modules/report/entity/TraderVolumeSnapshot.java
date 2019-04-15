/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 操盘手资产快照Entity
 * @author zzj
 * @version 2018-12-21
 */
public class TraderVolumeSnapshot extends DataEntity<TraderVolumeSnapshot> {
	
	private static final long serialVersionUID = 1L;
	private Date snapDate;		// 快照日期
	private String userId;		// user_id
	private String userTag;		// user_tag
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private Double tradeVolume;		// 币币
	private Double offlineVolume;		// c2c资产
	private Double lockVolume;		// 冻结资产
	private Double totalVolume;		// 总资产
	private Double billSumVolume;		// 流水汇总
	private Double balance;		// 差额
	private Double bobiVolume;		// 拨币资产
	private String remark;		// remark
	private String snapDateStr;
	
	public TraderVolumeSnapshot() {
		super();
	}

	public TraderVolumeSnapshot(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSnapDate() {
		return snapDate;
	}

	public void setSnapDate(Date snapDate) {
		this.snapDate = snapDate;
	}
	
	@Length(min=0, max=64, message="user_id长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=255, message="user_tag长度必须介于 0 和 255 之间")
	public String getUserTag() {
		return userTag;
	}

	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}
	
	@Length(min=0, max=64, message="币种id长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=45, message="币种代号长度必须介于 0 和 45 之间")
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
	
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	public Double getBobiVolume() {
		return bobiVolume;
	}

	public void setBobiVolume(Double bobiVolume) {
		this.bobiVolume = bobiVolume;
	}
	
	@Length(min=0, max=500, message="remark长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSnapDateStr() {
		return snapDateStr;
	}

	public void setSnapDateStr(String snapDateStr) {
		this.snapDateStr = snapDateStr;
	}
}