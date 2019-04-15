/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 挖矿规则Entity
 * @author dongfeng
 * @version 2018-08-07
 */
public class Mk2PopularizeMiningConf extends DataEntity<Mk2PopularizeMiningConf> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private BigDecimal totalVolume;		// 挖矿总量
	private BigDecimal grantVolume;		// 已挖总量
    private BigDecimal showMultiple;      // 已挖显示倍数
	private BigDecimal delayShowMultiple; // 已挖即将显示倍数
	private BigDecimal perVolume;		// 每次挖矿量
	private BigDecimal greaterVolume;		// 参于挖矿持有量
	private BigDecimal leaderGreaterVolume;	// 团队参于挖矿最小持有量
	private BigDecimal baseVolume;		// 团队挖矿基数
	private BigDecimal incomeHoldRatio;// 可得挖矿量与持用量占比
	private BigDecimal baseMultiple;// 团队挖矿基数倍数
	private String coinId;		// 币种ID
	private String coinSymbol;		// 币种名称
	private String status;		// 状态，0禁用1启用
	private String remark;		// 描述
	
	public Mk2PopularizeMiningConf() {
		super();
	}

	public Mk2PopularizeMiningConf(String id){
		super(id);
	}

	@Length(min=1, max=1, message="类型长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public BigDecimal getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public BigDecimal getGrantVolume() {
		return grantVolume;
	}

	public void setGrantVolume(BigDecimal grantVolume) {
		this.grantVolume = grantVolume;
	}
	
	public BigDecimal getPerVolume() {
		return perVolume;
	}

	public void setPerVolume(BigDecimal perVolume) {
		this.perVolume = perVolume;
	}
	
	public BigDecimal getGreaterVolume() {
		return greaterVolume;
	}

	public void setGreaterVolume(BigDecimal greaterVolume) {
		this.greaterVolume = greaterVolume;
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
	
	@Length(min=0, max=500, message="描述长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getBaseVolume() {
		return baseVolume;
	}

	public void setBaseVolume(BigDecimal baseVolume) {
		this.baseVolume = baseVolume;
	}

	public BigDecimal getIncomeHoldRatio() {
		return incomeHoldRatio;
	}

	public void setIncomeHoldRatio(BigDecimal incomeHoldRatio) {
		this.incomeHoldRatio = incomeHoldRatio;
	}

	public BigDecimal getBaseMultiple() {
		return baseMultiple;
	}

	public void setBaseMultiple(BigDecimal baseMultiple) {
		this.baseMultiple = baseMultiple;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getLeaderGreaterVolume() {
		return leaderGreaterVolume;
	}

	public void setLeaderGreaterVolume(BigDecimal leaderGreaterVolume) {
		this.leaderGreaterVolume = leaderGreaterVolume;
	}

    public BigDecimal getShowMultiple() {
        return showMultiple;
    }

    public void setShowMultiple(BigDecimal showMultiple) {
        this.showMultiple = showMultiple;
    }

	public BigDecimal getDelayShowMultiple() {
		return delayShowMultiple;
	}

	public void setDelayShowMultiple(BigDecimal delayShowMultiple) {
		this.delayShowMultiple = delayShowMultiple;
	}
}