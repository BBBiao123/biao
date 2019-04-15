/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 自动交易挂单Entity
 * @author zhangzijun
 * @version 2018-08-13
 */
public class MkAutoTradeOrder extends DataEntity<MkAutoTradeOrder> {
	
	private static final long serialVersionUID = 1L;
	private String settingId;		// setting_id
	private String monitorId;		// monitor_id
	private String userId;		// 用户id
	private String mail;		// 邮箱
	private String mobile;		// 手机
	private String type;		// 买卖类型
	private String status;		// 挂单状态
	private String coinMainSymbol;		// 主区币
	private String coinOtherSymbol;		// 被交易币
	private String price;		// 单价
	private String volume;		// 数量
	private String remark;		// 备注
	private String createByName;		// 创建人名称
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public MkAutoTradeOrder() {
		super();
	}

	public MkAutoTradeOrder(String id){
		super(id);
	}

	@Length(min=0, max=64, message="setting_id长度必须介于 0 和 64 之间")
	public String getSettingId() {
		return settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}
	
	@Length(min=0, max=64, message="monitor_id长度必须介于 0 和 64 之间")
	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	
	@Length(min=0, max=64, message="用户id长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
	
	@Length(min=0, max=1, message="买卖类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=1, message="挂单状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=12, message="主区币长度必须介于 0 和 12 之间")
	public String getCoinMainSymbol() {
		return coinMainSymbol;
	}

	public void setCoinMainSymbol(String coinMainSymbol) {
		this.coinMainSymbol = coinMainSymbol;
	}
	
	@Length(min=0, max=12, message="被交易币长度必须介于 0 和 12 之间")
	public String getCoinOtherSymbol() {
		return coinOtherSymbol;
	}

	public void setCoinOtherSymbol(String coinOtherSymbol) {
		this.coinOtherSymbol = coinOtherSymbol;
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	@Length(min=0, max=200, message="备注长度必须介于 0 和 200 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
}