/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 分红任务运行记录Entity
 * @author dongfeng
 * @version 2018-08-06
 */
public class Mk2PopularizeBonusTaskLog extends DataEntity<Mk2PopularizeBonusTaskLog> {
	
	private static final long serialVersionUID = 1L;
	private String status;		// 状态
	private String bonusVolume;		// 分红总量
	private String coinId;		// 分红币种ID
	private String coinSymbol;		// 分红币种名称
	private Date beginDate;		// 统计开始时间
	private Date endDate;		// 统计结束时间
	private String remark;		// 结果描述
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public Mk2PopularizeBonusTaskLog() {
		super();
	}

	public Mk2PopularizeBonusTaskLog(String id){
		super(id);
	}

	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getBonusVolume() {
		return bonusVolume;
	}

	public void setBonusVolume(String bonusVolume) {
		this.bonusVolume = bonusVolume;
	}
	
	@Length(min=1, max=64, message="分红币种ID长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=64, message="分红币种名称长度必须介于 1 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
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
	
	@Length(min=0, max=500, message="结果描述长度必须介于 0 和 500 之间")
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
		
}