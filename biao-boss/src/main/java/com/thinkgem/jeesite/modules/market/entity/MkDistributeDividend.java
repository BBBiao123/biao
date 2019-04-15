/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 分红规则Entity
 * @author zhangzijun
 * @version 2018-07-05
 */
public class MkDistributeDividend extends DataEntity<MkDistributeDividend> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 规则名称
	private String status;		// 状态
	private String percentage;		// 百分比（手续费）
	private String userId;		// 用户ID
	private String username;		// 用户名称
	private String coinId;		// 发放币种id
	private String coinSymbol;		// 发放币种代号
	private String platCoinId;		// 用户持有币种id
	private String platCoinSymbol;		// 用户持有币种代号
	private String grantVolume;		// 已发放数量
	private String usdtGrantVolume;		// USDT已发放数量
	private String btcGrantVolume;		// BTC已发放数量
	private String ethGrantVolume;		// ETH已发放数量
	private String remark;		// 说明
	
	public MkDistributeDividend() {
		super();
	}

	public MkDistributeDividend(String id){
		super(id);
	}

	@Length(min=0, max=100, message="规则名称长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
	//@Length(min=0, max=64, message="币种id长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	//@Length(min=0, max=45, message="币种代号长度必须介于 0 和 45 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public String getGrantVolume() {
		return grantVolume;
	}

	public void setGrantVolume(String grantVolume) {
		this.grantVolume = grantVolume;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@NotNull(message="user_id不能为空")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@NotNull(message="username不能为空")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NotNull(message="用户持有币种不能为空")
	public String getPlatCoinId() {
		return platCoinId;
	}

	public void setPlatCoinId(String platCoinId) {
		this.platCoinId = platCoinId;
	}

	@NotNull(message="用户持有币种不能为空")
	public String getPlatCoinSymbol() {
		return platCoinSymbol;
	}

	public void setPlatCoinSymbol(String platCoinSymbol) {
		this.platCoinSymbol = platCoinSymbol;
	}

	public String getUsdtGrantVolume() {
		return usdtGrantVolume;
	}

	public void setUsdtGrantVolume(String usdtGrantVolume) {
		this.usdtGrantVolume = usdtGrantVolume;
	}

	public String getBtcGrantVolume() {
		return btcGrantVolume;
	}

	public void setBtcGrantVolume(String btcGrantVolume) {
		this.btcGrantVolume = btcGrantVolume;
	}

	public String getEthGrantVolume() {
		return ethGrantVolume;
	}

	public void setEthGrantVolume(String ethGrantVolume) {
		this.ethGrantVolume = ethGrantVolume;
	}
}