/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

/**
 * 银商列表Entity
 * @author zzj
 * @version 2018-09-17
 */
public class OtcAgentInfo extends DataEntity<OtcAgentInfo> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 银商名称
	private String status;		// 状态
	private Integer number;		//自增序号
	private Double discount;		// 折扣
	private String sysUserId;		// 用户
	private String sysUserName;		// 用户登录名
	private String sysUserMail;		// 用户登录名
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private String remark;		// 说明
	private String[] userIds; 	//plat userIds
	
	public OtcAgentInfo() {
		super();
	}

	public OtcAgentInfo(String id){
		super(id);
	}

	@Length(min=1, max=20, message="银商名称长度必须介于 0 和 20 之间")
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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@DecimalMin(value = "0.01",message ="资金比例必须在0.01,1之间" )
	@DecimalMax(value = "1.00",message ="资金比例必须在0.01,1之间")
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	@Length(min=0, max=64, message="用户长度必须介于 0 和 64 之间")
	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	
	@Length(min=0, max=64, message="用户登录名长度必须介于 0 和 64 之间")
	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public String getSysUserMail() {
		return sysUserMail;
	}

	public void setSysUserMail(String sysUserMail) {
		this.sysUserMail = sysUserMail;
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
	
	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}
}