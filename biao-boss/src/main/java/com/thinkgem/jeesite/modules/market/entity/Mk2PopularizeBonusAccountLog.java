/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 平台运营分红日志Entity
 * @author dongfeng
 * @version 2018-08-06
 */
public class Mk2PopularizeBonusAccountLog extends DataEntity<Mk2PopularizeBonusAccountLog> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户ID
	private String mail;		// 邮箱
	private String mobile;		// mobile
	private String idCard;		// 身份证
	private String realName;		// 真实姓名
	private Date bonusDateBegin;		// 分红开始时间
	private Date bonusDateEnd;		// 分红结束时间
	private String coinId;		// 分红币种ID
	private String coinSymbol;		// 分红币种名称
	private String incomeVolume;		// 分红数量
	private String beforIncomeVolume;		// 分红前数量
	private String status;		// 状态
	private String remark;		// 描述
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public Mk2PopularizeBonusAccountLog() {
		super();
	}

	public Mk2PopularizeBonusAccountLog(String id){
		super(id);
	}

	@Length(min=1, max=1, message="用户ID长度必须介于 1 和 1 之间")
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBonusDateBegin() {
		return bonusDateBegin;
	}

	public void setBonusDateBegin(Date bonusDateBegin) {
		this.bonusDateBegin = bonusDateBegin;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBonusDateEnd() {
		return bonusDateEnd;
	}

	public void setBonusDateEnd(Date bonusDateEnd) {
		this.bonusDateEnd = bonusDateEnd;
	}
	
	@Length(min=1, max=64, message="分红币种ID长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=64, message="分红币种名称长度必须介于 1 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public String getIncomeVolume() {
		return incomeVolume;
	}

	public void setIncomeVolume(String incomeVolume) {
		this.incomeVolume = incomeVolume;
	}
	
	public String getBeforIncomeVolume() {
		return beforIncomeVolume;
	}

	public void setBeforIncomeVolume(String beforIncomeVolume) {
		this.beforIncomeVolume = beforIncomeVolume;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=500, message="描述长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		
}