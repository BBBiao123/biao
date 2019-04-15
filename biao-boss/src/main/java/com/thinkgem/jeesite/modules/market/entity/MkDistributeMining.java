/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 挖矿规则Entity
 * @author zhangzijun
 * @version 2018-07-05
 */
public class MkDistributeMining extends DataEntity<MkDistributeMining> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 规则名称
	private String status;		// 状态
	private String percentage;		// 百分比
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private String volume;		// 币种总数量
	private String grantVolume;		// 已发放数量
	private String remark;		// 已发放数量
	
	public MkDistributeMining() {
		super();
	}

	public MkDistributeMining(String id){
		super(id);
	}

	@Length(min=0, max=64, message="规则名称长度必须介于 0 和 64 之间")
	@NotNull(message="名称不能为空")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@NotNull(message="百分比不能为空")
	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	@NotNull(message="币种不能为空")
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

	@NotNull(message="币种总数量不能为空")
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	public String getGrantVolume() {
		return grantVolume;
	}

	public void setGrantVolume(String grantVolume) {
		this.grantVolume = grantVolume;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}