/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * c2c广告Entity
 * @author dazi
 * @version 2018-04-27
 */
public class OfflineOrder extends DataEntity<OfflineOrder> {

	private static final long serialVersionUID = 1L;
	private String userId;		// 用户id
	private String realName;		// 姓名
	private String coinId;		// 币种id
	private String symbol;		// 挂单币种标识
	private String volume;		// 挂单数量
	private String lockVolume;		// 锁定数量
	private String successVolume;		// 成交数量
	private String price;		// 价格
	private String totalPrice;		// 总价
	private String feeVolume;		// 手续费
	private String status;		// 状态
	private String flag;		// 记录标识
	private String exType;		// 广告类型
	private Date cancelDate; // 撤销时间

	private Long version;

	public OfflineOrder() {
		super();
	}

	public OfflineOrder(String id){
		super(id);
	}

	@Length(min=1, max=64, message="用户id长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Length(min=0, max=64, message="姓名长度必须介于 0 和 64 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Length(min=0, max=64, message="币种id长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	@Length(min=1, max=11, message="挂单币种标识长度必须介于 1 和 11 之间")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getLockVolume() {
		return lockVolume;
	}

	public void setLockVolume(String lockVolume) {
		this.lockVolume = lockVolume;
	}

	public String getSuccessVolume() {
		return successVolume;
	}

	public void setSuccessVolume(String successVolume) {
		this.successVolume = successVolume;
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

	public String getFeeVolume() {
		return feeVolume;
	}

	public void setFeeVolume(String feeVolume) {
		this.feeVolume = feeVolume;
	}

	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Length(min=0, max=1, message="记录标识长度必须介于 0 和 1 之间")
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Length(min=0, max=1, message="广告类型长度必须介于 0 和 1 之间")
	public String getExType() {
		return exType;
	}

	public void setExType(String exType) {
		this.exType = exType;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}