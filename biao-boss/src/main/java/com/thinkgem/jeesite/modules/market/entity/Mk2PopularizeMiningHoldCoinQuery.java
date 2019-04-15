/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 挖矿持币量查询Entity
 * @author dongfeng
 * @version 2018-09-06
 */
public class Mk2PopularizeMiningHoldCoinQuery extends DataEntity<Mk2PopularizeMiningHoldCoinQuery> {
	
	private static final long serialVersionUID = 1L;
	private String userId;	// 用户ID
	private String type;		// 类别
	private BigDecimal holdVolume;		// 持币数量
	private Date countDate;		// 挖矿时间
	
	public Mk2PopularizeMiningHoldCoinQuery() {
		super();
	}

	public Mk2PopularizeMiningHoldCoinQuery(String id){
		super(id);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getHoldVolume() {
		return holdVolume;
	}

	public void setHoldVolume(BigDecimal holdVolume) {
		this.holdVolume = holdVolume;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}
}