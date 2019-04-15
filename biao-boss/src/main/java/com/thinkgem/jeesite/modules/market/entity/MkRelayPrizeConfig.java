/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 接力撞奖配置Entity
 * @author zzj
 * @version 2018-08-31
 */
public class MkRelayPrizeConfig extends DataEntity<MkRelayPrizeConfig> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 规则名称
	private String status;		// 状态
	private BigDecimal volume;		// 奖金总量
	private BigDecimal startVolume;		// 起步奖金
	private BigDecimal stepAddVolume;		// 新增奖金
	private String beginTime;		// 每天开始时间
	private String endTime;		// 每天结束时间
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private String userId;		// 归集账户ID
	private String username;		// 归集账户名称
	private String mail;		// 归集账户邮箱
	private String mobile;		// 归集账户手机
	private String realName;		// 归集账户真实姓名
    private String isRemit;     //是否打款
	private BigDecimal grantVolume;		// 已发放数量
	private BigDecimal curPoolVolume;		// 当前奖池总量
	private BigDecimal curPoolNumber; //
	private BigDecimal minVolume;	//用户最低参与数量
	private String remark;		// 说明
	
	public MkRelayPrizeConfig() {
		super();
	}

	public MkRelayPrizeConfig(String id){
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

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getStartVolume() {
		return startVolume;
	}

	public void setStartVolume(BigDecimal startVolume) {
		this.startVolume = startVolume;
	}

	public BigDecimal getStepAddVolume() {
		return stepAddVolume;
	}

	public void setStepAddVolume(BigDecimal stepAddVolume) {
		this.stepAddVolume = stepAddVolume;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIsRemit() {
		return isRemit;
	}

	public void setIsRemit(String isRemit) {
		this.isRemit = isRemit;
	}

	public BigDecimal getGrantVolume() {
		return grantVolume;
	}

	public void setGrantVolume(BigDecimal grantVolume) {
		this.grantVolume = grantVolume;
	}

	public BigDecimal getCurPoolVolume() {
		return curPoolVolume;
	}

	public void setCurPoolVolume(BigDecimal curPoolVolume) {
		this.curPoolVolume = curPoolVolume;
	}

	public BigDecimal getCurPoolNumber() {
		return curPoolNumber;
	}

	public void setCurPoolNumber(BigDecimal curPoolNumber) {
		this.curPoolNumber = curPoolNumber;
	}

	public BigDecimal getMinVolume() {
		return minVolume;
	}

	public void setMinVolume(BigDecimal minVolume) {
		this.minVolume = minVolume;
	}

	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}