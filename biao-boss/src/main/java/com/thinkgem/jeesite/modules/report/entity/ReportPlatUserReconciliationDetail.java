/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * ddEntity
 * @author zzj
 * @version 2018-10-10
 */
public class ReportPlatUserReconciliationDetail extends DataEntity<ReportPlatUserReconciliationDetail> {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String mail;
	private String mobile;
	private String coinSymbol;		// 币种
	private String type;		// 类型
	private Double volume;		// 资产

	public ReportPlatUserReconciliationDetail() {
		super();
	}

	public ReportPlatUserReconciliationDetail(String id){
		super(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCoinSymbol() {
		return this.coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
}