/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 营销营销账户资产流水Entity
 * @author zhangzijun
 * @version 2018-07-06
 */
public class MkDistributeLog extends DataEntity<MkDistributeLog> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 营销规则类型
	private String userId;		// 用户ID
	private String username;		// 用户名
	private String mail;		// 邮箱
	private String mobile;		// 手机号码
	private String coinId;		// 币种ID
	private String coinSymbol;		// 币符号
	private String volume;		// 币种数量
	private String status;		// 状态
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private Date beginTaskDate;		// 开始 任务日期
	private Date endTaskDate;		// 结束 任务日期
	private String remark;
	private Date taskDate;		// 任务日期
	
	public MkDistributeLog() {
		super();
	}

	public MkDistributeLog(String id){
		super(id);
	}

	@Length(min=1, max=1, message="营销规则类型长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=1, max=64, message="用户ID长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=1, max=45, message="用户名长度必须介于 1 和 45 之间")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=1, max=64, message="币种ID长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=64, message="币符号长度必须介于 1 和 64 之间")
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
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getBeginTaskDate() {
		return beginTaskDate;
	}

	public void setBeginTaskDate(Date beginTaskDate) {
		this.beginTaskDate = beginTaskDate;
	}

	public Date getEndTaskDate() {
		return endTaskDate;
	}

	public void setEndTaskDate(Date endTaskDate) {
		this.endTaskDate = endTaskDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}
}