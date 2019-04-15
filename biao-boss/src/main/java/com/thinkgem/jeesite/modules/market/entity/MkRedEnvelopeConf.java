/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 红包配置Entity
 * @author zijun
 * @version 2019-01-25
 */
public class MkRedEnvelopeConf extends DataEntity<MkRedEnvelopeConf> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private Double singleLowerVolume;		// 金额下限
	private Double singleHigherVolume;		// 金额上限
	private Integer lowerNumber;		// 个数下限
	private Integer higherNumber;		// 个数下限
	private String fee;		// 手续费
	private String destroyUserId;		// 手续费归集账户
	private Double luckyLowerVolume;		// 手气金额下限
	private Double luckyHigherVolume;		// 手气金额上限
	private Integer pointVolume;		// 价格精度
	private String status;		// 状态
	private String remark;		// 备注
	private Integer version;		// 版本号
	
	public MkRedEnvelopeConf() {
		super();
	}

	public MkRedEnvelopeConf(String id){
		super(id);
	}

	@Length(min=0, max=100, message="名称长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public Double getSingleLowerVolume() {
		return singleLowerVolume;
	}

	public void setSingleLowerVolume(Double singleLowerVolume) {
		this.singleLowerVolume = singleLowerVolume;
	}
	
	public Double getSingleHigherVolume() {
		return singleHigherVolume;
	}

	public void setSingleHigherVolume(Double singleHigherVolume) {
		this.singleHigherVolume = singleHigherVolume;
	}
	
	public Integer getLowerNumber() {
		return lowerNumber;
	}

	public void setLowerNumber(Integer lowerNumber) {
		this.lowerNumber = lowerNumber;
	}
	
	public Integer getHigherNumber() {
		return higherNumber;
	}

	public void setHigherNumber(Integer higherNumber) {
		this.higherNumber = higherNumber;
	}
	
	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
	
	@Length(min=0, max=64, message="手续费归集账户长度必须介于 0 和 64 之间")
	public String getDestroyUserId() {
		return destroyUserId;
	}

	public void setDestroyUserId(String destroyUserId) {
		this.destroyUserId = destroyUserId;
	}
	
	public Double getLuckyLowerVolume() {
		return luckyLowerVolume;
	}

	public void setLuckyLowerVolume(Double luckyLowerVolume) {
		this.luckyLowerVolume = luckyLowerVolume;
	}
	
	public Double getLuckyHigherVolume() {
		return luckyHigherVolume;
	}

	public void setLuckyHigherVolume(Double luckyHigherVolume) {
		this.luckyHigherVolume = luckyHigherVolume;
	}
	
	public Integer getPointVolume() {
		return pointVolume;
	}

	public void setPointVolume(Integer pointVolume) {
		this.pointVolume = pointVolume;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=500, message="备注长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}