/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * c2c银商和广告商对账Entity
 * @author ruoyu
 * @version 2018-10-31
 */
public class JsPlatOfflineCoinVolumeDay extends DataEntity<JsPlatOfflineCoinVolumeDay> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户id
	private String mobile;		// 手机号
	private String mail;		// 邮箱
	private String tag;		// 用户tag标签
	private Date countDay;		// 统计时间
	private String buyTotal;		// 收入金额
	private String sellTotal;		// 支出金额
	private String surplusTotal;		// 余额
	private Date createTime;		// 创建时间
	
	public JsPlatOfflineCoinVolumeDay() {
		super();
	}

	public JsPlatOfflineCoinVolumeDay(String id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=20, message="手机号长度必须介于 0 和 20 之间")
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
	
	@Length(min=0, max=64, message="用户tag标签长度必须介于 0 和 64 之间")
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCountDay() {
		return countDay;
	}

	public void setCountDay(Date countDay) {
		this.countDay = countDay;
	}
	
	public String getBuyTotal() {
		return buyTotal;
	}

	public void setBuyTotal(String buyTotal) {
		this.buyTotal = buyTotal;
	}
	
	public String getSellTotal() {
		return sellTotal;
	}

	public void setSellTotal(String sellTotal) {
		this.sellTotal = sellTotal;
	}
	
	public String getSurplusTotal() {
		return surplusTotal;
	}

	public void setSurplusTotal(String surplusTotal) {
		this.surplusTotal = surplusTotal;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}