/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 主区币兑人民币汇率Entity
 * @author dongfeng
 * @version 2018-08-21
 */
public class JsPlatMainCnb extends DataEntity<JsPlatMainCnb> {
	
	private static final long serialVersionUID = 1L;
	private String coinId;		// 主区币ID
	private String coinSymbol;		// 主区币名称
	private Double cnbRate;		// 兑人民币汇率
	
	public JsPlatMainCnb() {
		super();
	}

	public JsPlatMainCnb(String id){
		super(id);
	}

	@Length(min=0, max=64, message="主区币ID长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=64, message="主区币名称长度必须介于 0 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public Double getCnbRate() {
		return cnbRate;
	}

	public void setCnbRate(Double cnbRate) {
		this.cnbRate = cnbRate;
	}
	
}