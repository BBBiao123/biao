/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 币种归集Entity
 * @author tt
 * @version 2019-03-18
 */
public class CoinCollection extends DataEntity<CoinCollection> {
	
	private static final long serialVersionUID = 1L;
	private String symbol;		// symbol
	private String userId;		// user_id
	private String address;		// address
	private BigDecimal volume;		// volume
	private String status;		// 0 未完成  1 归集完成
	
	public CoinCollection() {
		super();
	}

	public CoinCollection(String id){
		super(id);
	}

	@Length(min=1, max=100, message="symbol长度必须介于 1 和 100 之间")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	@Length(min=1, max=100, message="user_id长度必须介于 1 和 100 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=1, max=256, message="address长度必须介于 1 和 256 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	
	@Length(min=1, max=11, message="0 未完成  1 归集完成长度必须介于 1 和 11 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}