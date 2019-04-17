/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.List;

/**
 * ddEntity
 * @author zzj
 * @version 2018-10-10
 */
public class ReportPlatUserFinance extends DataEntity<ReportPlatUserFinance> {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String realName;
	private String mail;
	private String mobile;
	private  String idCard;
	private String coinSymbol;
	private Double cbvolume;
	private Double tbvolume;
	private Double yingkui;

	private Integer inviteCode;
	private Integer referInviteCode;

	private List<String> userList;

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	private List<ReportPlatUserFinance> childList;

	public List<ReportPlatUserFinance> getChildList() {
		return childList;
	}

	public void setChildList(List<ReportPlatUserFinance> childList) {
		this.childList = childList;
	}

	public ReportPlatUserFinance() {
		super();
	}

	public Integer getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(Integer inviteCode) {
		this.inviteCode = inviteCode;
	}

	public Integer getReferInviteCode() {
		return referInviteCode;
	}

	public void setReferInviteCode(Integer referInviteCode) {
		this.referInviteCode = referInviteCode;
	}

	public ReportPlatUserFinance(String id){
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
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}

	public Double getCbvolume() {
		return cbvolume;
	}

	public void setCbvolume(Double cbvolume) {
		this.cbvolume = cbvolume;
	}

	public Double getTbvolume() {
		return tbvolume;
	}

	public void setTbvolume(Double tbvolume) {
		this.tbvolume = tbvolume;
	}

	public Double getYingkui() {
		return yingkui;
	}

	public void setYingkui(Double yingkui) {
		this.yingkui = yingkui;
	}
}