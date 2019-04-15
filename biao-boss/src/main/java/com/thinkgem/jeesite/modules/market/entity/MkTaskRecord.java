/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 营销任务执行记录Entity
 * @author zhangzijun
 * @version 2018-07-06
 */
public class MkTaskRecord extends DataEntity<MkTaskRecord> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 营销类型
	private Date taskDate;		// 任务日期
	private Date executeTime;	//执行时间
	private String coinId;		// 币种ID
	private String coinSymbol;		// 币符号
	private String volume;		// volume
	private String status;		// 状态，1-成功,0-失败
	private String remark;		// 说明
	private Date beginTaskDate;		// 开始 任务日期
	private Date endTaskDate;		// 结束 任务日期
	
	public MkTaskRecord() {
		super();
	}

	public MkTaskRecord(String id){
		super(id);
	}

	@Length(min=0, max=1, message="营销类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}
	
	@Length(min=0, max=64, message="币种ID长度必须介于 0 和 64 之间")
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
	
	@Length(min=0, max=1, message="状态，1-成功,0-失败长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}
}