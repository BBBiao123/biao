/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 超级钱包资产Entity
 * @author zzj
 * @version 2018-12-25
 */
public class SuperCoinVolume extends DataEntity<SuperCoinVolume> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户ID
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private String volume;		// 币种数量
	private Date depositBegin;		// 定期存开始时间
	private Date depositEnd;		// 定期存结束时间
	private String version;		// 版本号乐观锁
	private Long lockCycle; //锁定周期
	private Long remainingDays; //剩余天数

	public SuperCoinVolume() {
		super();
	}

	public SuperCoinVolume(String id){
		super(id);
	}

	@Length(min=1, max=64, message="用户ID长度必须介于 1 和 64 之间")
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDepositBegin() {
		return depositBegin;
	}

	public void setDepositBegin(Date depositBegin) {
		this.depositBegin = depositBegin;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDepositEnd() {
		return depositEnd;
	}

	public void setDepositEnd(Date depositEnd) {
		this.depositEnd = depositEnd;
	}
	
	@Length(min=1, max=10, message="版本号乐观锁长度必须介于 1 和 10 之间")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getLockCycle() {
		if(Objects.nonNull(this.getDepositEnd())){
			Duration duration = java.time.Duration.between(this.toLocalDateTime(this.getDepositBegin()),this.toLocalDateTime(this.getDepositEnd()));
			return duration.toDays();
		}
		return 0L;
	}

	public void setLockCycle(Long lockCycle) {
		this.lockCycle = lockCycle;
	}

	public Long getRemainingDays() {
		if(Objects.nonNull(this.getDepositEnd())){
			Duration duration = java.time.Duration.between(LocalDateTime.now(),this.toLocalDateTime(this.getDepositEnd()));
			return duration.toDays();
		}
		return 0L;
	}

	public void setRemainingDays(Long remainingDays) {
		this.remainingDays = remainingDays;
	}

	public LocalDateTime toLocalDateTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, zone);
	}
}