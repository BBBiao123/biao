/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 挖矿任务日志Entity
 * @author dongfeng
 * @version 2018-08-07
 */
public class Mk2PopularizeMiningTaskLog extends DataEntity<Mk2PopularizeMiningTaskLog> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String coinId;		// 币种ID
	private String coinSymbol;		// 币种名称
	private BigDecimal miningVolume;		// 本次总量
	private BigDecimal grantVolume;		// 送出挖矿总量
	private Date countDate;		// 挖矿时间
	private String status;		// 状态
	private String remark;		// 结果
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public Mk2PopularizeMiningTaskLog() {
		super();
	}

	public Mk2PopularizeMiningTaskLog(String id){
		super(id);
	}

	@Length(min=1, max=1, message="类型长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=64, message="币种ID长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=64, message="币种名称长度必须介于 0 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public BigDecimal getMiningVolume() {
		return miningVolume;
	}

	public void setMiningVolume(BigDecimal miningVolume) {
		this.miningVolume = miningVolume;
	}
	
	public BigDecimal getGrantVolume() {
		return grantVolume;
	}

	public void setGrantVolume(BigDecimal grantVolume) {
		this.grantVolume = grantVolume;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
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