/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 抽奖活动规则Entity
 * @author zzj
 * @version 2018-11-01
 */
public class MkLuckyDrawConfig extends DataEntity<MkLuckyDrawConfig> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 活动名称
	private Integer periods;		// 期数
	private String status;		// 状态
	private Double volume;		// 奖金总量
	private Double startVolume;		// 起步奖金
	private Double stepAddVolume;		// 新增奖金
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private Double grantVolume;		// 已发放数量
	private Double poolVolume;		// 当前奖池总量
	private Double minVolume;		// 用户最低参与数量
	private Double deductFee;		// 扣除手续费
	private Double fee;		// 收纳手续费
	private Integer playerNumber;		// 参与人数
	private String remark;		// 说明
	
	public MkLuckyDrawConfig() {
		super();
	}

	public MkLuckyDrawConfig(String id){
		super(id);
	}

	@Length(min=0, max=100, message="活动名称长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	public Double getStartVolume() {
		return startVolume;
	}

	public void setStartVolume(Double startVolume) {
		this.startVolume = startVolume;
	}
	
	public Double getStepAddVolume() {
		return stepAddVolume;
	}

	public void setStepAddVolume(Double stepAddVolume) {
		this.stepAddVolume = stepAddVolume;
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
	
	public Double getMinVolume() {
		return minVolume;
	}

	public void setMinVolume(Double minVolume) {
		this.minVolume = minVolume;
	}
	
	public Double getDeductFee() {
		return deductFee;
	}

	public void setDeductFee(Double deductFee) {
		this.deductFee = deductFee;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Integer getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(Integer playerNumber) {
		this.playerNumber = playerNumber;
	}

	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}