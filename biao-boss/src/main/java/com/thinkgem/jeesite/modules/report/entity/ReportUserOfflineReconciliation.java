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
 * @version 2018-11-26
 */
public class ReportUserOfflineReconciliation extends DataEntity<ReportUserOfflineReconciliation> {

	private static final long serialVersionUID = 1L;
	private String userMobile;		// 手机
	private Double volume;		// 数量
	private Double price;		// 单价
	private Double totalPrice;		// 总价
	private Date confirmReceiptDate;		// 确认收款日期
	private String memo;		// 备注
	private Date beginDayTime;		// 开始日期
	private Date endDayTime;		// 结束日期
	private String endDateTimeStr; //

	public ReportUserOfflineReconciliation() {
		super();
	}

	public ReportUserOfflineReconciliation(String id){
		super(id);
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getConfirmReceiptDate() {
		return confirmReceiptDate;
	}

	public void setConfirmReceiptDate(Date confirmReceiptDate) {
		this.confirmReceiptDate = confirmReceiptDate;
	}

	public Date getBeginDayTime() {
		return beginDayTime;
	}

	public void setBeginDayTime(Date beginDayTime) {
		this.beginDayTime = beginDayTime;
	}

	public Date getEndDayTime() {
		return endDayTime;
	}

	public void setEndDayTime(Date endDayTime) {
		this.endDayTime = endDayTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getEndDateTimeStr() {
		return DateUtils.formatDate(this.getEndDayTime());
	}

	public void setEndDateTimeStr(String endDateTimeStr) {
		this.endDateTimeStr = endDateTimeStr;
	}
}