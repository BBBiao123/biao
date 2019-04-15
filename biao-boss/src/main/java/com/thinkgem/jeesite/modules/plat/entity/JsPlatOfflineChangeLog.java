/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * C2C转账记录Entity
 * @author zzj
 * @version 2018-10-26
 */
public class JsPlatOfflineChangeLog extends DataEntity<JsPlatOfflineChangeLog> {
	
	private static final long serialVersionUID = 1L;
	private String changeNo;		// 订单号
	private String status;		// 状态
	private String userId;		// 用户ID
	private String account;		// 账户
	private String realName;		// 真实姓名
	private String coinId;		// 币id
	private String coinSymbol;		// 币种符号
	private String volume;		// 转账数额
	private String type;		// 类型
	private String fee;		// 手续费
	private String otherUserId;		// 被交易用户ID
	private String otherAccount;		// 对象账户
	private String otherRealName;		// 对象账户真实姓名
	
	public JsPlatOfflineChangeLog() {
		super();
	}

	public JsPlatOfflineChangeLog(String id){
		super(id);
	}

	@Length(min=0, max=64, message="订单号长度必须介于 0 和 64 之间")
	public String getChangeNo() {
		return changeNo;
	}

	public void setChangeNo(String changeNo) {
		this.changeNo = changeNo;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=64, message="用户ID长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=64, message="账户长度必须介于 0 和 64 之间")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@Length(min=0, max=45, message="真实姓名长度必须介于 0 和 45 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Length(min=0, max=64, message="币id长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=64, message="币种符号长度必须介于 0 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	@Length(min=0, max=1, message="类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
	
	@Length(min=0, max=64, message="被交易用户ID长度必须介于 0 和 64 之间")
	public String getOtherUserId() {
		return otherUserId;
	}

	public void setOtherUserId(String otherUserId) {
		this.otherUserId = otherUserId;
	}
	
	@Length(min=0, max=64, message="对象账户长度必须介于 0 和 64 之间")
	public String getOtherAccount() {
		return otherAccount;
	}

	public void setOtherAccount(String otherAccount) {
		this.otherAccount = otherAccount;
	}
	
	@Length(min=0, max=45, message="对象账户真实姓名长度必须介于 0 和 45 之间")
	public String getOtherRealName() {
		return otherRealName;
	}

	public void setOtherRealName(String otherRealName) {
		this.otherRealName = otherRealName;
	}
	
}