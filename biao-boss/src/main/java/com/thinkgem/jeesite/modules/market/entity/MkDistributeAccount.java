/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 营销账户Entity
 * @author zhangzijun
 * @version 2018-07-05
 */
public class MkDistributeAccount extends DataEntity<MkDistributeAccount> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 账户类型
	private String name;		// 账户名称
	private String status;		// 状态
	private String userId;		// user_id
	private String username;		// 用户名
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private String releaseVolume;		// 释放币种数量
	private String lockVolume;		// 冻结资产
	private String mail;		// 邮箱
	private String mobile;		// 手机
	private String idCard;		// 身份证
	private String realName;		// 真实姓名
	
	public MkDistributeAccount() {
		super();
	}

	public MkDistributeAccount(String id){
		super(id);
	}

	@Length(min=1, max=1, message="账户类型长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=1, max=100, message="账户名称长度必须介于 1 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@NotNull(message="user_id不能为空")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=1, max=64, message="币种id长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=45, message="币种代号长度必须介于 1 和 45 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public String getReleaseVolume() {
		return releaseVolume;
	}

	public void setReleaseVolume(String releaseVolume) {
		this.releaseVolume = releaseVolume;
	}
	
	public String getLockVolume() {
		return lockVolume;
	}

	public void setLockVolume(String lockVolume) {
		this.lockVolume = lockVolume;
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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
}