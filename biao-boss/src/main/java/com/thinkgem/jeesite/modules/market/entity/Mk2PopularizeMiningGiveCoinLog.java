/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 挖矿规则送币流水Entity
 * @author dongfeng
 * @version 2018-08-07
 */
public class Mk2PopularizeMiningGiveCoinLog extends DataEntity<Mk2PopularizeMiningGiveCoinLog> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String userId;		// 用户ID
	private String mail;		// 邮箱
	private String mobile;		// mobile
	private String idCard;		// 身份证
	private String realName;		// 真实姓名
	private String coinId;		// 币种ID
	private String coinSymbol;		// 币种名称
	private BigDecimal volume;		// 送币量
	private BigDecimal totalVolume;		// 本次总量
	private BigDecimal ratio;		// 占总量百分比
	private Long orderNo;		// 排名
	private BigDecimal joinVolume;		// 本次参与挖矿占比的数量
	private BigDecimal maxSubVolume;		// 本次大区挖矿占比数量
	private Date countDate;		// 挖矿时间
	private Date beginCountDate;		// 开始 挖矿时间
	private Date endCountDate;		// 结束 挖矿时间
	private BigDecimal teamHoldTotal; // 团队持币量
	
	public Mk2PopularizeMiningGiveCoinLog() {
		super();
	}

	public Mk2PopularizeMiningGiveCoinLog(String id){
		super(id);
	}

	@Length(min=1, max=1, message="类型长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=1, max=64, message="用户ID长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=64, message="邮箱长度必须介于 0 和 64 之间")
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@Length(min=0, max=11, message="mobile长度必须介于 0 和 11 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=18, message="身份证长度必须介于 0 和 18 之间")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	@Length(min=0, max=45, message="真实姓名长度必须介于 0 和 45 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Length(min=1, max=64, message="币种ID长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=64, message="币种名称长度必须介于 1 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	
	public BigDecimal getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}
	
	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	
	public BigDecimal getJoinVolume() {
		return joinVolume;
	}

	public void setJoinVolume(BigDecimal joinVolume) {
		this.joinVolume = joinVolume;
	}
	
	public BigDecimal getMaxSubVolume() {
		return maxSubVolume;
	}

	public void setMaxSubVolume(BigDecimal maxSubVolume) {
		this.maxSubVolume = maxSubVolume;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}
	
	public Date getBeginCountDate() {
		return beginCountDate;
	}

	public void setBeginCountDate(Date beginCountDate) {
		this.beginCountDate = beginCountDate;
	}
	
	public Date getEndCountDate() {
		return endCountDate;
	}

	public void setEndCountDate(Date endCountDate) {
		this.endCountDate = endCountDate;
	}

	public BigDecimal getTeamHoldTotal() {
		return teamHoldTotal;
	}

	public void setTeamHoldTotal(BigDecimal teamHoldTotal) {
		this.teamHoldTotal = teamHoldTotal;
	}
}