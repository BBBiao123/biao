/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 日手续费按币种统计Entity
 * @author ruoyu
 * @version 2018-06-26
 */
public class ReportTradeFeeCoin extends DataEntity<ReportTradeFeeCoin> {
	
	private static final long serialVersionUID = 1L;
	private String sumFee;		// 手续费
	private String coin;		// 币种
	private Date countTime;		// 统计时间
	private Date createTime;		// 创建时间
	
	private Date startTime ;
	private Date endTime ;
	
	public ReportTradeFeeCoin() {
		super();
	}

	public ReportTradeFeeCoin(String id){
		super(id);
	}

	public String getSumFee() {
		return sumFee;
	}

	public void setSumFee(String sumFee) {
		this.sumFee = sumFee;
	}
	
	@Length(min=0, max=20, message="币种长度必须介于 0 和 20 之间")
	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
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