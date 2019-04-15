/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 会员推广Entity
 * @author zhangzijun
 * @version 2018-07-05
 */
public class MkDistributePromote extends DataEntity<MkDistributePromote> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 规则名称
	private String status;		// 状态
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private String volume;		// 推广总量
	private String grantVolume;		// 已发放数量
	
	public MkDistributePromote() {
		super();
	}

	public MkDistributePromote(String id){
		super(id);
	}

	@Length(min=0, max=100, message="规则名称长度必须介于 0 和 100 之间")
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
	
}