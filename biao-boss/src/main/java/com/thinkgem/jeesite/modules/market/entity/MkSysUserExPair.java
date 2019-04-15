/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 营销用户币币对Entity
 * @author zzj
 * @version 2018-08-23
 */
public class MkSysUserExPair extends DataEntity<MkSysUserExPair> {
	
	private static final long serialVersionUID = 1L;
	private String sysUserId;		// sys_user_id
	private String sysUserName;		// 登录名称
	private String userId;		// userId
	private String username;		// 用户名
	private String mobile;		// 手机
	private String mail;		// 邮箱
	private String realName;		// 真实姓名
	private String idCard;		// 身份证
	private String exPairId;		// ex_pair_id
	private String exPairSymbol;		// ex_pair_symbol
	private String coinMainId;		// coin_main_id
	private String coinMainSymbol;		// coin_main_symbol
	private String coinOtherId;		// coin_other_id
	private String coinOtherSymbol;		// coin_other_symbol
	private String remark;		// remark
	
	public MkSysUserExPair() {
		super();
	}

	public MkSysUserExPair(String id){
		super(id);
	}

	@Length(min=0, max=64, message="sys_user_id长度必须介于 0 和 64 之间")
	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	
	@Length(min=0, max=100, message="登录名称长度必须介于 0 和 100 之间")
	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}
	
	@Length(min=0, max=64, message="ex_pair_id长度必须介于 0 和 64 之间")
	public String getExPairId() {
		return exPairId;
	}

	public void setExPairId(String exPairId) {
		this.exPairId = exPairId;
	}
	
	@Length(min=0, max=64, message="ex_pair_symbol长度必须介于 0 和 64 之间")
	public String getExPairSymbol() {
		return exPairSymbol;
	}

	public void setExPairSymbol(String exPairSymbol) {
		this.exPairSymbol = exPairSymbol;
	}
	
	@Length(min=0, max=64, message="coin_main_id长度必须介于 0 和 64 之间")
	public String getCoinMainId() {
		return coinMainId;
	}

	public void setCoinMainId(String coinMainId) {
		this.coinMainId = coinMainId;
	}
	
	@Length(min=0, max=64, message="coin_main_symbol长度必须介于 0 和 64 之间")
	public String getCoinMainSymbol() {
		return coinMainSymbol;
	}

	public void setCoinMainSymbol(String coinMainSymbol) {
		this.coinMainSymbol = coinMainSymbol;
	}
	
	@Length(min=0, max=64, message="coin_other_id长度必须介于 0 和 64 之间")
	public String getCoinOtherId() {
		return coinOtherId;
	}

	public void setCoinOtherId(String coinOtherId) {
		this.coinOtherId = coinOtherId;
	}
	
	@Length(min=0, max=64, message="coin_other_symbol长度必须介于 0 和 64 之间")
	public String getCoinOtherSymbol() {
		return coinOtherSymbol;
	}

	public void setCoinOtherSymbol(String coinOtherSymbol) {
		this.coinOtherSymbol = coinOtherSymbol;
	}
	
	@Length(min=0, max=500, message="remark长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
}