/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 自动交易用户Entity
 * @author zhangzijun
 * @version 2018-08-17
 */
public class MkAutoTradeUser extends DataEntity<MkAutoTradeUser> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// userId
	private String username;		// 用户名
	private String mobile;		// 手机
	private String mail;		// 邮箱
	private String realName;		// 真实姓名
	private String idCard;		// 身份证
	private String source;		// 来源
	private String remark;		// 说明
	
	public MkAutoTradeUser() {
		super();
	}

	public MkAutoTradeUser(String id){
		super(id);
	}

	@Length(min=0, max=64, message="userId长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=45, message="用户名长度必须介于 0 和 45 之间")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=0, max=11, message="手机长度必须介于 0 和 11 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=64, message="邮箱长度必须介于 0 和 64 之间")
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@Length(min=0, max=45, message="真实姓名长度必须介于 0 和 45 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Length(min=0, max=18, message="身份证长度必须介于 0 和 18 之间")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}