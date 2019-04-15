/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * c2c流水统计表Entity
 * @author ruoyu
 * @version 2018-10-24
 */
public class JsPlatOfflineOrderDetailLog extends DataEntity<JsPlatOfflineOrderDetailLog> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户id
	private String coinId;		// 币种id
	private String symbol;		// 币种符号
	private String buyTotal;		// 买入统计值
	private String sellTotal;		// 卖出统计值
	private String surplusTotal;		// 结余统计值
	private Date countDate;		// 统计时间
	private Date countLessDate ;
	
	private String mobile ;
	
	public JsPlatOfflineOrderDetailLog() {
		super();
	}

	public JsPlatOfflineOrderDetailLog(String id){
		super(id);
	}

	@Length(min=0, max=128, message="用户id长度必须介于 0 和 128 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=64, message="币种id长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=64, message="币种符号长度必须介于 0 和 64 之间")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
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
	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}

	public Date getCountLessDate() {
		return countLessDate;
	}

	public void setCountLessDate(Date countLessDate) {
		this.countLessDate = countLessDate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}