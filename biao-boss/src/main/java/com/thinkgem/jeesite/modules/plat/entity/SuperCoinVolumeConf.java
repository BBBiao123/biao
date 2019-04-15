/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 超级钱包配置Entity
 * @author zzj
 * @version 2018-12-25
 */
public class SuperCoinVolumeConf extends DataEntity<SuperCoinVolumeConf> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private Double inMinVolume;		// 最小转入数量
	private Double outMinVolume;		// 最小转出数量
	private Double multiple;		// 放大倍数(超级钱包)
	private Double memberLockMultiple;		// 放大倍数(冻结)
	private Integer lockCycle;		// 锁定周期（天）
	private Integer frozenDay;		// 冻结天数
	private Double breakRatio;		// 违约金比例
	private String destroyUserId;		// 销毁总账户
	private String transferStatus;		// 划转状态
	private String status;		// 状态
	
	public SuperCoinVolumeConf() {
		super();
	}

	public SuperCoinVolumeConf(String id){
		super(id);
	}

	@Length(min=0, max=64, message="名称长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=64, message="币种id长度必须介于 1 和 64 之间")
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
	
	public Double getInMinVolume() {
		return inMinVolume;
	}

	public void setInMinVolume(Double inMinVolume) {
		this.inMinVolume = inMinVolume;
	}
	
	public Double getOutMinVolume() {
		return outMinVolume;
	}

	public void setOutMinVolume(Double outMinVolume) {
		this.outMinVolume = outMinVolume;
	}
	
	public Double getMultiple() {
		return multiple;
	}

	public void setMultiple(Double multiple) {
		this.multiple = multiple;
	}
	
	public Integer getLockCycle() {
		return lockCycle;
	}

	public void setLockCycle(Integer lockCycle) {
		this.lockCycle = lockCycle;
	}
	
	public Integer getFrozenDay() {
		return frozenDay;
	}

	public void setFrozenDay(Integer frozenDay) {
		this.frozenDay = frozenDay;
	}
	
	public Double getBreakRatio() {
		return breakRatio;
	}

	public void setBreakRatio(Double breakRatio) {
		this.breakRatio = breakRatio;
	}
	
	@Length(min=0, max=64, message="销毁总账户长度必须介于 0 和 64 之间")
	public String getDestroyUserId() {
		return destroyUserId;
	}

	public void setDestroyUserId(String destroyUserId) {
		this.destroyUserId = destroyUserId;
	}
	
	@Length(min=0, max=1, message="划转状态长度必须介于 0 和 1 之间")
	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getMemberLockMultiple() {
		return memberLockMultiple;
	}

	public void setMemberLockMultiple(Double memberLockMultiple) {
		this.memberLockMultiple = memberLockMultiple;
	}
}