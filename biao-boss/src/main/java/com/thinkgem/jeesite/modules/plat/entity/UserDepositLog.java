/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户充值管理Entity
 * @author dazi
 * @version 2018-05-04
 */
public class UserDepositLog extends DataEntity<UserDepositLog> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户id
	private String coinId;		// 币id
	private String address;		// 充值地址
	private String coinSymbol;		// 币符号
	private String txId;		// 充值hash
	private String volume;		// volume
	private String status;		// status
	
	private Integer raiseStatus ;
	
	private Date startTime ;
	private Date endTime ;
	
	private String withdrawVolume ;
	private String depositVolume ;

    private String mobile ;//手机
    private String mail ;//邮箱
	
	public UserDepositLog() {
		super();
	}

	public UserDepositLog(String id){
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
	
	@Length(min=1, max=64, message="充值地址长度必须介于 1 和 64 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=1, max=64, message="币符号长度必须介于 1 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	@Length(min=0, max=128, message="充值hash长度必须介于 0 和 128 之间")
	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	@Length(min=0, max=1, message="status长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getRaiseStatus() {
		return raiseStatus;
	}

	public void setRaiseStatus(Integer raiseStatus) {
		this.raiseStatus = raiseStatus;
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