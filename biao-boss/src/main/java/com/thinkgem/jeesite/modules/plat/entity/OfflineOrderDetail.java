/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * c2c广告详情Entity
 * @author dazi
 * @version 2018-04-30
 */
public class OfflineOrderDetail extends DataEntity<OfflineOrderDetail> {
	
	private static final long serialVersionUID = 1L;
	private String orderId;		// 广告id
	private String subOrderId;		// 订单号
	private String volume;		// 成交数量
	private String coinId;		// 币种id
	private String symbol;		// 币种标识
	private String price;		// 价格
	private String totalPrice;		// 总价
	private String userId;		// 用户id
	private String askUserId;		// 被交易用户id
	private String status;		// 状态
	private String radomNum;		// radom_num
	private String syncDate;		// sync_date
	
	public OfflineOrderDetail() {
		super();
	}

	public OfflineOrderDetail(String id){
		super(id);
	}

	@Length(min=1, max=64, message="广告id长度必须介于 1 和 64 之间")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Length(min=0, max=64, message="订单号长度必须介于 0 和 64 之间")
	public String getSubOrderId() {
		return subOrderId;
	}

	public void setSubOrderId(String subOrderId) {
		this.subOrderId = subOrderId;
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	@Length(min=0, max=64, message="币种id长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=11, message="币种标识长度必须介于 1 和 11 之间")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	@Length(min=1, max=64, message="用户id长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=1, max=64, message="被交易用户id长度必须介于 1 和 64 之间")
	public String getAskUserId() {
		return askUserId;
	}

	public void setAskUserId(String askUserId) {
		this.askUserId = askUserId;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=6, message="radom_num长度必须介于 0 和 6 之间")
	public String getRadomNum() {
		return radomNum;
	}

	public void setRadomNum(String radomNum) {
		this.radomNum = radomNum;
	}
	
	public String getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(String syncDate) {
		this.syncDate = syncDate;
	}
	
}