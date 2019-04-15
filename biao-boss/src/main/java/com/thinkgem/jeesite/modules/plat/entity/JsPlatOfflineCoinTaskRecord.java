/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * C2C币种价格更新记录Entity
 * @author zzj
 * @version 2018-10-09
 */
public class JsPlatOfflineCoinTaskRecord extends DataEntity<JsPlatOfflineCoinTaskRecord> {
	
	private static final long serialVersionUID = 1L;
	private String status;		// 状态
	private String symbol;		// 币种标识
	private String coinId;		// 币种id
	private Double beforeMaxPrice;		// 最大价格（前）
	private Double beforeMinPrice;		// 最低价格（前）
	private Double dayIncPrice;		// 价格增量
	private String remark;		// 备注
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public JsPlatOfflineCoinTaskRecord() {
		super();
	}

	public JsPlatOfflineCoinTaskRecord(String id){
		super(id);
	}

	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=11, message="币种标识长度必须介于 0 和 11 之间")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	@Length(min=0, max=64, message="币种id长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	public Double getBeforeMaxPrice() {
		return beforeMaxPrice;
	}

	public void setBeforeMaxPrice(Double beforeMaxPrice) {
		this.beforeMaxPrice = beforeMaxPrice;
	}
	
	public Double getBeforeMinPrice() {
		return beforeMinPrice;
	}

	public void setBeforeMinPrice(Double beforeMinPrice) {
		this.beforeMinPrice = beforeMinPrice;
	}
	
	public Double getDayIncPrice() {
		return dayIncPrice;
	}

	public void setDayIncPrice(Double dayIncPrice) {
		this.dayIncPrice = dayIncPrice;
	}
	
	@Length(min=0, max=500, message="备注长度必须介于 0 和 500 之间")
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