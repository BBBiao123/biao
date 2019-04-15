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
public class ReportPlatUserInvite extends DataEntity<ReportPlatUserInvite> {

	private static final long serialVersionUID = 1L;
	private String userId;
	private Integer inviteCode;
	private Integer referInviteCode;

	private List<ReportPlatUserInvite> childList;

	public List<ReportPlatUserInvite> getChildList() {
		return childList;
	}

	public void setChildList(List<ReportPlatUserInvite> childList) {
		this.childList = childList;
	}

	public ReportPlatUserInvite() {
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

	public ReportPlatUserInvite(String id){
		super(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}