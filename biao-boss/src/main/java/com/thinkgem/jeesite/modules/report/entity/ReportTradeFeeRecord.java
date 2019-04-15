/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 日手续费按交易对统计Entity
 * @author ruoyu
 * @version 2018-06-26
 */
public class ReportTradeFeeRecord extends DataEntity<ReportTradeFeeRecord> {
	
	private static final long serialVersionUID = 1L;
	private String coinMain;		// 主币
	private String coinOther;		// 被交易币
	private String mainFree;		// 主币手续费
	private String otherFree;		// 被交易币手续费
	private Date countTime;		// 统计时间
	private Date createTime;		// 创建时间
	
	private Date startTime ;
	
	private Date endTime ;
	
	public ReportTradeFeeRecord() {
		super();
	}

	public ReportTradeFeeRecord(String id){
		super(id);
	}

	@Length(min=0, max=20, message="主币长度必须介于 0 和 20 之间")
	public String getCoinMain() {
		return coinMain;
	}

	public void setCoinMain(String coinMain) {
		this.coinMain = coinMain;
	}
	
	@Length(min=0, max=20, message="被交易币长度必须介于 0 和 20 之间")
	public String getCoinOther() {
		return coinOther;
	}

	public void setCoinOther(String coinOther) {
		this.coinOther = coinOther;
	}
	
	public String getMainFree() {
		return mainFree;
	}

	public void setMainFree(String mainFree) {
		this.mainFree = mainFree;
	}
	
	public String getOtherFree() {
		return otherFree;
	}

	public void setOtherFree(String otherFree) {
		this.otherFree = otherFree;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCountTime() {
		return countTime;
	}

	public void setCountTime(Date countTime) {
		this.countTime = countTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}