/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 分红规则明细Entity
 * @author zhangzijun
 * @version 2018-07-16
 */
public class MkDistributeDividendDetail extends DataEntity<MkDistributeDividendDetail> {
	
	private static final long serialVersionUID = 1L;
	private String dividendId;		// 分红规则ID
	private String percentage;		// 百分比
	private String accountType;		// 账户类型
	private String userId;		// user_id
	private String username;		// 用户名
	private String remark;   //说明
	
	public MkDistributeDividendDetail() {
		super();
	}

	public MkDistributeDividendDetail(String id){
		super(id);
	}

	@Length(min=0, max=64, message="分红规则ID长度必须介于 0 和 64 之间")
	public String getDividendId() {
		return dividendId;
	}

	public void setDividendId(String dividendId) {
		this.dividendId = dividendId;
	}
	
	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
	@Length(min=0, max=1, message="账户类型长度必须介于 0 和 1 之间")
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	@Length(min=0, max=64, message="user_id长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=45, message="用户名长度必须介于 0 和 45 之间")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}