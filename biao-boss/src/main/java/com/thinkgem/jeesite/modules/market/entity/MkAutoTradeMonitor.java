/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 自动交易监控Entity
 * @author zhangzijun
 * @version 2018-08-13
 */
public class MkAutoTradeMonitor extends DataEntity<MkAutoTradeMonitor> {
	
	private static final long serialVersionUID = 1L;
	private String settingId;		// setting_id
	private String type;		// 买卖类型
	private String status;		// 状态
	private String userId;		// 用户ID
	private String username;		// 用户名
	private String mail;		// 邮箱
	private String mobile;		// 手机
	private String coinMainSymbol;		// 主区币
	private String coinOtherSymbol;		// 被交易币
	private Date beginDate;		// 开始时间
	private Date endDate;		// 结束时间
	private String minVolume;		// 最小成交量
	private String maxVolume;		// 最大成交量
	private String minPrice;		// 最低价
	private String maxPrice;		// 最高价
	private String frequency;		// 频率
	private String timeUnit;		// 时间单位
	private String orderNumber;		// 下单笔数
	private String orderVolume;		// 下单数量
	private String orderPrice;		// 下单金额
	private Date orderBeginDate;   //下单开始时间
	private Date orderEndDate;     //下单结束时间
	private String remark;		// 说明
	private String createByName;
	private String busType;
	
	public MkAutoTradeMonitor() {
		super();
	}

	public MkAutoTradeMonitor(String id){
		super(id);
	}

	@Length(min=0, max=64, message="setting_id长度必须介于 0 和 64 之间")
	public String getSettingId() {
		return settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}
	
	@Length(min=0, max=1, message="买卖类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=64, message="用户ID长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=64, message="用户名长度必须介于 0 和 64 之间")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=0, max=64, message="邮箱长度必须介于 0 和 64 之间")
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@Length(min=0, max=11, message="手机长度必须介于 0 和 11 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=64, message="主区币长度必须介于 0 和 64 之间")
	public String getCoinMainSymbol() {
		return coinMainSymbol;
	}

	public void setCoinMainSymbol(String coinMainSymbol) {
		this.coinMainSymbol = coinMainSymbol;
	}
	
	@Length(min=0, max=64, message="被交易币长度必须介于 0 和 64 之间")
	public String getCoinOtherSymbol() {
		return coinOtherSymbol;
	}

	public void setCoinOtherSymbol(String coinOtherSymbol) {
		this.coinOtherSymbol = coinOtherSymbol;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getMinVolume() {
		return minVolume;
	}

	public void setMinVolume(String minVolume) {
		this.minVolume = minVolume;
	}
	
	public String getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(String maxVolume) {
		this.maxVolume = maxVolume;
	}
	
	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}
	
	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	@Length(min=0, max=10, message="频率长度必须介于 0 和 10 之间")
	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	@Length(min=0, max=2, message="时间单位长度必须介于 0 和 2 之间")
	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	@Length(min=0, max=9, message="下单笔数长度必须介于 0 和 9 之间")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getOrderVolume() {
		return orderVolume;
	}

	public void setOrderVolume(String orderVolume) {
		this.orderVolume = orderVolume;
	}
	
	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public Date getOrderBeginDate() {
		return orderBeginDate;
	}

	public void setOrderBeginDate(Date orderBeginDate) {
		this.orderBeginDate = orderBeginDate;
	}

	public Date getOrderEndDate() {
		return orderEndDate;
	}

	public void setOrderEndDate(Date orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
}