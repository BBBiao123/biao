/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户提现管理Entity
 * @author dazi
 * @version 2018-05-04
 */
public class UserWithdrawLog extends DataEntity<UserWithdrawLog> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户id
	private String coinId;		// 币id
	private String address;		// 提现地址
	private String coinSymbol;		// 提现币种符号
	private String txId;		// 交易hash
	private String status;		// 状态
	private BigDecimal volume;		// 提币数量
	private BigDecimal realVolume;		// 提币数量
	private BigDecimal fee;		// 提币数量
	private String remark;		// 描述
	private  Date auditDate;	// 更新日期
	
	private String auditReason ;

	private Integer confirms;
	private Integer confirmStatus;
	private Date startTime ;
	private Date endTime ;
	
	private String withdrawVolume ;
	private String depositVolume ;

	private String mobile ;//手机
	private String mail ;//邮箱
	
	public UserWithdrawLog() {
		super();
	}

	public UserWithdrawLog(String id){
		super(id);
	}

	@Length(min=1, max=64, message="用户id长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=1, max=64, message="币id长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=64, message="提现地址长度必须介于 1 和 64 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=1, max=64, message="提现币种符号长度必须介于 1 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	@Length(min=0, max=128, message="交易hash长度必须介于 0 和 128 之间")
	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getRealVolume() {
		return realVolume;
	}

	public void setRealVolume(BigDecimal realVolume) {
		this.realVolume = realVolume;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getWithdrawVolume() {
		return withdrawVolume;
	}

	public void setWithdrawVolume(String withdrawVolume) {
		this.withdrawVolume = withdrawVolume;
	}

	public String getDepositVolume() {
		return depositVolume;
	}

	public void setDepositVolume(String depositVolume) {
		this.depositVolume = depositVolume;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

	public Integer getConfirms() {
		return confirms;
	}

	public void setConfirms(Integer confirms) {
		this.confirms = confirms;
	}

	public Integer getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(Integer confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
}