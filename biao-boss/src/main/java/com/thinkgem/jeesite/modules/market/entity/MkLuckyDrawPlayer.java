/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 抽奖活动参与者Entity
 * @author zzj
 * @version 2018-11-01
 */
public class MkLuckyDrawPlayer extends DataEntity<MkLuckyDrawPlayer> {
	
	private static final long serialVersionUID = 1L;
	private Integer periods;		// 期数
	private String status;		// 状态
	private String userId;		// PlatUserId
	private String mail;		// 用户名称
	private String mobile;		// 手机
	private String realName;		// 真实姓名
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private Double volume;		// 参与时币种数量
	private Double deductFee;		// 扣除费用
	private String remark;		// 说明
	private Date drawDate;		// 开奖时间
	private Double luckyVolume;		// 奖金数量
	
	public MkLuckyDrawPlayer() {
		super();
	}

	public MkLuckyDrawPlayer(String id){
		super(id);
	}

	public Integer getPeriods() {
		return periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=64, message="PlatUserId长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=64, message="用户名称长度必须介于 0 和 64 之间")
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@Length(min=0, max=64, message="手机长度必须介于 0 和 64 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=64, message="真实姓名长度必须介于 0 和 64 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Length(min=0, max=64, message="币种id长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=45, message="币种代号长度必须介于 0 和 45 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	public Double getDeductFee() {
		return deductFee;
	}

	public void setDeductFee(Double deductFee) {
		this.deductFee = deductFee;
	}
	
	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(Date drawDate) {
		this.drawDate = drawDate;
	}

	public Double getLuckyVolume() {
		return luckyVolume;
	}

	public void setLuckyVolume(Double luckyVolume) {
		this.luckyVolume = luckyVolume;
	}
}