/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 注册活动抽奖记录Entity
 * @author xiaoyu
 * @version 2018-10-25
 */
public class MkUserRegisterLotteryLog extends DataEntity<MkUserRegisterLotteryLog> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户id
	private String lotteryName;		// 活动名称
	private String ruleId;		// 规则id
	private String lotteryId;		// lottery_id
	private String coinSymbol;		// 币种
	private BigDecimal realVolume;		// 数量
	private String reason;		// 原因
	private String mail;		// 邮箱
	private String phone;		// 会员手机号
	private String recommendId;		// 推荐人id
	private Date phoneDate;		// 会员绑定手机时间
	private String source;		// 来源, iso web android


	private Date beginPhoneDate;		// 绑定手机开始时间
	private Date endPhoneDate;		// 绑定手机结束时间

	public Date getBeginPhoneDate() {
		return beginPhoneDate;
	}

	public void setBeginPhoneDate(Date beginPhoneDate) {
		this.beginPhoneDate = beginPhoneDate;
	}

	public Date getEndPhoneDate() {
		return endPhoneDate;
	}

	public void setEndPhoneDate(Date endPhoneDate) {
		this.endPhoneDate = endPhoneDate;
	}

	public BigDecimal getRealVolume() {
		return realVolume;
	}

	public void setRealVolume(BigDecimal realVolume) {
		this.realVolume = realVolume;
	}

	public MkUserRegisterLotteryLog() {
		super();
	}

	public MkUserRegisterLotteryLog(String id){
		super(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Length(min=0, max=100, message="活动名称长度必须介于 0 和 100 之间")
	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}
	
	@Length(min=1, max=64, message="规则id长度必须介于 1 和 64 之间")
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
	@Length(min=0, max=64, message="lottery_id长度必须介于 0 和 64 之间")
	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	
	@Length(min=1, max=64, message="币种长度必须介于 1 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	

	
	@Length(min=1, max=100, message="原因长度必须介于 1 和 100 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Length(min=0, max=100, message="邮箱长度必须介于 0 和 100 之间")
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@Length(min=0, max=100, message="会员手机号长度必须介于 0 和 100 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPhoneDate() {
		return phoneDate;
	}

	public void setPhoneDate(Date phoneDate) {
		this.phoneDate = phoneDate;
	}
	
	@Length(min=0, max=16, message="来源, iso web android长度必须介于 0 和 16 之间")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}