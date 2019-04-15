/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import com.thinkgem.jeesite.common.utils.DateUtils;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * 币种销毁记录Entity
 * @author zzj
 * @version 2018-10-09
 */
public class MkCoinDestroyRecord extends DataEntity<MkCoinDestroyRecord> {
	
	private static final long serialVersionUID = 1L;
	private String symbol;		// 币种标识
	private String coinId;		// 币种id
	private Double volume;		// 销毁数量
	private Date destroyDate;	// 销毁日期
	private String destroyDateStr;	// 销毁日期
	private String remark;		// 备注
	
	public MkCoinDestroyRecord() {
		super();
	}

	public MkCoinDestroyRecord(String id){
		super(id);
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
	
	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Date getDestroyDate() {
		return destroyDate;
	}

	public void setDestroyDate(Date destroyDate) {
		this.destroyDate = destroyDate;
	}

	public String getDestroyDateStr() {
		return ObjectUtils.isEmpty(this.getDestroyDate()) ? null : DateUtils.formatDateTime(this.getDestroyDate());
	}

	public void setDestroyDateStr(String destroyDateStr) {
		this.destroyDateStr = destroyDateStr;
	}

	@Length(min=0, max=500, message="备注长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}