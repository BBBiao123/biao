/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 平台收益流水Entity
 * @author dongfeng
 * @version 2018-08-09
 */
public class MkCommonUserCoinFee extends DataEntity<MkCommonUserCoinFee> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户ID
	private String mail;		// 邮箱
	private String mobile;		// mobile
	private String idCard;		// 身份证
	private String realName;		// 真实姓名
	private String coinId;		// 币种ID
	private String coinSymbol;		// 币种名称
	private Double volume;		// 手续费量
	private Double exUsdtVol;		// 兑换USDT量
	private Date beginDate;		// 统计开始时间
	private Date endDate;		// 统计结束时间
	private Date beginBeginDate;		// 开始 统计开始时间
	private Date endBeginDate;		// 结束 统计开始时间
	private Date beginEndDate;		// 开始 统计结束时间
	private Date endEndDate;		// 结束 统计结束时间
	
	public MkCommonUserCoinFee() {
		super();
	}

	public MkCommonUserCoinFee(String id){
		super(id);
	}

	@Length(min=0, max=64, message="用户ID长度必须介于 0 和 64 之间")
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
	
	@Length(min=0, max=64, message="币种ID长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=64, message="币种名称长度必须介于 0 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	@NotNull(message="手续费量不能为空")
	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	public Double getExUsdtVol() {
		return exUsdtVol;
	}

	public void setExUsdtVol(Double exUsdtVol) {
		this.exUsdtVol = exUsdtVol;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getBeginBeginDate() {
		return beginBeginDate;
	}

	public void setBeginBeginDate(Date beginBeginDate) {
		this.beginBeginDate = beginBeginDate;
	}
	
	public Date getEndBeginDate() {
		return endBeginDate;
	}

	public void setEndBeginDate(Date endBeginDate) {
		this.endBeginDate = endBeginDate;
	}
		
	public Date getBeginEndDate() {
		return beginEndDate;
	}

	public void setBeginEndDate(Date beginEndDate) {
		this.beginEndDate = beginEndDate;
	}
	
	public Date getEndEndDate() {
		return endEndDate;
	}

	public void setEndEndDate(Date endEndDate) {
		this.endEndDate = endEndDate;
	}
		
}