/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;

import java.util.Date;

/**
 * ddEntity
 * @author zzj
 * @version 2018-10-08
 */
public class ReportOfflineFee extends DataEntity<ReportOfflineFee> {

	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String number;		// 笔数
	private String fee;		// 手续费
	private Date beginCreateDate;		// 开始日期
	private Date endCreateDate;		// 结束日期
	private String endCreateDateStr;		// 结束日期Str

	public ReportOfflineFee() {
		super();
	}

	public ReportOfflineFee(String id){
		super(id);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
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

	public String getEndCreateDateStr() {
		return DateUtils.formatDate(this.getEndCreateDate());
	}

	public void setEndCreateDateStr(String endCreateDateStr) {
		this.endCreateDateStr = endCreateDateStr;
	}
}