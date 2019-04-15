/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 币币资产Entity
 * @author dazi
 * @version 2018-04-27
 */
public class UserCoinVolume extends DataEntity<UserCoinVolume> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// user_id
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private BigDecimal volume;		// 币种数量
	private BigDecimal lockVolume;		// 用户交易冻结资产
	private BigDecimal outLockVolume;// 用户提现冻结资产
	private BigDecimal totalVolume; //总资产
	private String mail;
	private String mobile;
	
	private String redisVolume ;
	private String redisLockVolume ;
	
	private String airdropId ;
	
	public UserCoinVolume() {
		super();
	}

	public UserCoinVolume(String id){
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

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getLockVolume() {
		return lockVolume;
	}

	public void setLockVolume(BigDecimal lockVolume) {
		this.lockVolume = lockVolume;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public BigDecimal getTotalVolume() {
		return lockVolume.add(volume);
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}

	public BigDecimal getOutLockVolume() {
		return outLockVolume;
	}

	public void setOutLockVolume(BigDecimal outLockVolume) {
		this.outLockVolume = outLockVolume;
	}

	public String getRedisVolume() {
		return redisVolume;
	}

	public void setRedisVolume(String redisVolume) {
		this.redisVolume = redisVolume;
	}

	public String getRedisLockVolume() {
		return redisLockVolume;
	}

	public void setRedisLockVolume(String redisLockVolume) {
		this.redisLockVolume = redisLockVolume;
	}

	public String getAirdropId() {
		return airdropId;
	}

	public void setAirdropId(String airdropId) {
		this.airdropId = airdropId;
	}
	
}