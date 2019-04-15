/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户空头币种Entity
 * @author ruoyu
 * @version 2018-11-15
 */
public class JsPlatUserAirdrop extends DataEntity<JsPlatUserAirdrop> {
	
	private static final long serialVersionUID = 1L;
	private String coinSymbol;		// 币种符号
	private String coinId;		// 币种id
	private String userType;		// 0:所有用户  1:实名用户
	private String number;		// 数量
	private Date createTime;		// 空头时间
	private String mark;		// 备注
	private Date endTime;		// 用户截止时间
	private Date startTime;		// 用户截止时间
	private String status;		// 记录状态  0:开始空头  1:空头中  2:空头完成
	
	public JsPlatUserAirdrop() {
		super();
	}

	public JsPlatUserAirdrop(String id){
		super(id);
	}

	@Length(min=0, max=64, message="币种符号长度必须介于 0 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	@Length(min=0, max=128, message="币种id长度必须介于 0 和 128 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=11, message="0:所有用户  1:实名用户长度必须介于 0 和 11 之间")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	@Length(min=0, max=11, message="数量长度必须介于 0 和 11 之间")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Length(min=0, max=128, message="备注长度必须介于 0 和 128 之间")
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Length(min=0, max=11, message="记录状态  0:开始空头  1:空头中  2:空头完成长度必须介于 0 和 11 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}