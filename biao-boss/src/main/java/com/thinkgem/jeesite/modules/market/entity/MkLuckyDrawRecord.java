/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 抽奖活动开奖记录Entity
 * @author zzj
 * @version 2018-11-01
 */
public class MkLuckyDrawRecord extends DataEntity<MkLuckyDrawRecord> {
	
	private static final long serialVersionUID = 1L;
	private String status;		// 状态
	private Integer periods;		// 期数
	private Double volume;		// 奖金总量
	private Double grantVolume;		// 已发放数量
	private Double poolVolume;		// 奖池总量
	private Integer playerNumber;		// 参与人数
	private Double luckyVolume;		// 奖金数量
	private Double deductFee;		// 总手续费
	private String coinId;		// 币种ID
	private String coinSymbol;		// 币符号
	private String userId;		// 中奖人id
	private String mail;		// 中奖人邮箱
	private String mobile;		// 中奖人手机
	private String realName;		// 真实姓名
	private String remark;		// remark
	private Integer luckyNumber;		// 幸运随机号码
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public MkLuckyDrawRecord() {
		super();
	}

	public MkLuckyDrawRecord(String id){
		super(id);
	}

	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Integer getPeriods() {
		return periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}
	
	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	public Double getGrantVolume() {
		return grantVolume;
	}

	public void setGrantVolume(Double grantVolume) {
		this.grantVolume = grantVolume;
	}
	
	public Double getPoolVolume() {
		return poolVolume;
	}

	public void setPoolVolume(Double poolVolume) {
		this.poolVolume = poolVolume;
	}
	
	public Integer getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(Integer playerNumber) {
		this.playerNumber = playerNumber;
	}
	
	public Double getLuckyVolume() {
		return luckyVolume;
	}

	public void setLuckyVolume(Double luckyVolume) {
		this.luckyVolume = luckyVolume;
	}
	
	public Double getDeductFee() {
		return deductFee;
	}

	public void setDeductFee(Double deductFee) {
		this.deductFee = deductFee;
	}
	
	@Length(min=0, max=64, message="币种ID长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=64, message="币符号长度必须介于 1 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	@Length(min=0, max=64, message="中奖人id长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=64, message="中奖人邮箱长度必须介于 0 和 64 之间")
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@Length(min=0, max=64, message="中奖人手机长度必须介于 0 和 64 之间")
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

	public Integer getLuckyNumber() {
		return luckyNumber;
	}

	public void setLuckyNumber(Integer luckyNumber) {
		this.luckyNumber = luckyNumber;
	}

	@Length(min=0, max=500, message="remark长度必须介于 0 和 500 之间")
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