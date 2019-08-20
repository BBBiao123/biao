/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 币币资产Entity
 * @author dazi
 * @version 2018-04-27
 */
public class UserChangeRecordVolume extends DataEntity<UserChangeRecordVolume> {

	private static final long serialVersionUID = 1L;
	private String userId;		// user_id
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号

	private BigDecimal totalVolume; //总资产
	private String mail;
	private String mobile;
	private String flag;

	private String redisVolume ;
	private String redisLockVolume ;

	private String airdropId ;

	private BigDecimal  coinNum;

	private String type;

	private Date createDate;



	private BigDecimal detailReward;

	private String rewardType;

	private Date incomeDate;


	private BigDecimal coinVolume;

	private BigDecimal sumBalance;

	private BigDecimal sumVolume;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}

	public BigDecimal getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public BigDecimal getCoinNum() {
		return coinNum;
	}

	public void setCoinNum(BigDecimal coinNum) {
		this.coinNum = coinNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getDetailReward() {
		return detailReward;
	}

	public void setDetailReward(BigDecimal detailReward) {
		this.detailReward = detailReward;
	}

	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	public Date getIncomeDate() {
		return incomeDate;
	}

	public void setIncomeDate(Date incomeDate) {
		this.incomeDate = incomeDate;
	}

	public BigDecimal getCoinVolume() {
		return coinVolume;
	}

	public void setCoinVolume(BigDecimal coinVolume) {
		this.coinVolume = coinVolume;
	}

	public BigDecimal getSumBalance() {
		return sumBalance;
	}

	public void setSumBalance(BigDecimal sumBalance) {
		this.sumBalance = sumBalance;
	}

	public BigDecimal getSumVolume() {
		return sumVolume;
	}

	public void setSumVolume(BigDecimal sumVolume) {
		this.sumVolume = sumVolume;
	}
}