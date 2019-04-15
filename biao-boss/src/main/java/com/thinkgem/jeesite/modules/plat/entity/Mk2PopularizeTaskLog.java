/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * mk2营销任务执行结果Entity
 * @author dongfeng
 * @version 2018-07-20
 */
public class Mk2PopularizeTaskLog extends DataEntity<Mk2PopularizeTaskLog> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 任务类型
	private String typeName;		// 任务名称
	private Long dayGiveColume; // 当天送币总量
	private String paramTaskDate;		// 任务参数开始时间
	private String status;		// 执行结果
	private String reason;		// 错误原因
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private Date executeTime;		// 结束 创建时间

	public Mk2PopularizeTaskLog() {
		super();
	}

	public Mk2PopularizeTaskLog(String id){
		super(id);
	}

	@Length(min=1, max=1, message="任务类型长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=1, max=50, message="任务名称长度必须介于 1 和 50 之间")
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@Length(min=1, max=20, message="任务参数开始时间长度必须介于 1 和 20 之间")
	public String getParamTaskDate() {
		return paramTaskDate;
	}

	public void setParamTaskDate(String paramTaskDate) {
		this.paramTaskDate = paramTaskDate;
	}
	
	@Length(min=1, max=1, message="执行结果长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=200, message="错误原因长度必须介于 0 和 200 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public Long getDayGiveColume() {
		return dayGiveColume;
	}

	public void setDayGiveColume(Long dayGiveColume) {
		this.dayGiveColume = dayGiveColume;
	}

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}
}