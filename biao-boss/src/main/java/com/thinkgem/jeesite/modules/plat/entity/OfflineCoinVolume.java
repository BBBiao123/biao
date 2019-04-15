/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * c2c资产Entity
 * @author dazi
 * @version 2018-04-27
 */
public class OfflineCoinVolume extends DataEntity<OfflineCoinVolume> {

	private static final long serialVersionUID = 1L;
	private String userId;		// user_id
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private String volume;		// 币种数量
	private String advertVolume;		// 广告冻结资产
	private String lockVolume;		// 交易冻结资产
	private String bailVolume;
	private Long version;

	public OfflineCoinVolume() {
		super();
	}

	public OfflineCoinVolume(String id){
		super(id);
	}

	@Length(min=1, max=64, message="user_id长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getAdvertVolume() {
		return advertVolume;
	}

	public void setAdvertVolume(String advertVolume) {
		this.advertVolume = advertVolume;
	}

	public String getLockVolume() {
		return lockVolume;
	}

	public void setLockVolume(String lockVolume) {
		this.lockVolume = lockVolume;
	}

	public String getBailVolume() {
		return bailVolume;
	}

	public void setBailVolume(String bailVolume) {
		this.bailVolume = bailVolume;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}