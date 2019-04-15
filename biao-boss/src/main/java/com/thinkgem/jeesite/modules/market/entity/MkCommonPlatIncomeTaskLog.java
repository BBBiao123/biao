/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 平台收入任务日志Entity
 * @author dongfeng
 * @version 2018-08-09
 */
public class MkCommonPlatIncomeTaskLog extends DataEntity<MkCommonPlatIncomeTaskLog> {
	
	private static final long serialVersionUID = 1L;
	private Date beginDate;		// 开始时间
	private Date endDate;		// 结束时间
	private String status;		// 状态
	private String remark;		// 结果
	private Date beginBeginDate;		// 开始 开始时间
	private Date endBeginDate;		// 结束 开始时间
	private Date beginEndDate;		// 开始 结束时间
	private Date endEndDate;		// 结束 结束时间
	
	public MkCommonPlatIncomeTaskLog() {
		super();
	}

	public MkCommonPlatIncomeTaskLog(String id){
		super(id);
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
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=500, message="结果长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginBeginDate() {
		return beginBeginDate;
	}

	public void setBeginBeginDate(Date beginBeginDate) {
		this.beginBeginDate = beginBeginDate;
	}
	
	public Date getEndBeginDate() {
		return endBeginDate;
	}

	public void setEndBeginDate(Date endBeginDate) {
		this.endBeginDate = endBeginDate;
	}
		
	public Date getBeginEndDate() {
		return beginEndDate;
	}

	public void setBeginEndDate(Date beginEndDate) {
		this.beginEndDate = beginEndDate;
	}
	
	public Date getEndEndDate() {
		return endEndDate;
	}

	public void setEndEndDate(Date endEndDate) {
		this.endEndDate = endEndDate;
	}
		
}