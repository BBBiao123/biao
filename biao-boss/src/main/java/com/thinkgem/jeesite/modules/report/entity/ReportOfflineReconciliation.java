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
public class ReportOfflineReconciliation extends DataEntity<ReportOfflineReconciliation> {

	private static final long serialVersionUID = 1L;
	private String mobile;		// 手机
	private String mail;		// 交易副币
	private String dayTime;		// 日期
	private Double income;		// 收入
	private Double payout;		// 支出
	private Double balance;		// 余额
	private String tag;		//
	private Date beginDayTime;		// 开始日期
	private Date endDayTime;		// 结束日期
	private String endDateTimeStr; //

	public ReportOfflineReconciliation() {
		super();
	}

	public ReportOfflineReconciliation(String id){
		super(id);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDayTime() {
		return dayTime;
	}

	public void setDayTime(String dayTime) {
		this.dayTime = dayTime;
	}

	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}

	public Double getPayout() {
		return payout;
	}

	public void setPayout(Double payout) {
		this.payout = payout;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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

	public String getEndDateTimeStr() {
		return DateUtils.formatDate(this.getEndDayTime());
	}

	public void setEndDateTimeStr(String endDateTimeStr) {
		this.endDateTimeStr = endDateTimeStr;
	}
}