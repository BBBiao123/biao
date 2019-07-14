/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 用户资产账单Entity
 * @author ruoyu
 * @version 2019-01-02
 */
public class JsPlatUserCoinVolumeBill extends DataEntity<JsPlatUserCoinVolumeBill> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private String coinSymbol;		// 币总信息
	private String priority;		// 优先级 默认5
	private String refKey;		// 关联KEY
	private Integer opSign;		// 操作符号
	private BigDecimal opLockVolume;		// 操作lock_volume的数量.
	private BigDecimal opVolume;		// 变化的数量
	private Integer forceLock; // 是否强制拿锁
	private String source;		// 来源
	private String mark;		// 备注信息
	private Integer status;		// 执行状态
	private Integer hash;		// 确定哪台服务器拉取到当前的数据.
	
	private String opSignText ;
	
	public JsPlatUserCoinVolumeBill() {
		super();
	}

	public JsPlatUserCoinVolumeBill(String id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public String getRefKey() {
		return refKey;
	}

	public void setRefKey(String refKey) {
		this.refKey = refKey;
	}
	
	public Integer getOpSign() {
		return opSign;
	}

	public void setOpSign(Integer opSign) {
		this.opSign = opSign;
	}
	
	public BigDecimal getOpLockVolume() {
		return opLockVolume;
	}

	public void setOpLockVolume(BigDecimal opLockVolume) {
		this.opLockVolume = opLockVolume;
	}
	
	public BigDecimal getOpVolume() {
		return opVolume;
	}

	public void setOpVolume(BigDecimal opVolume) {
		this.opVolume = opVolume;
	}
	
	@Length(min=1, max=64, message="来源长度必须介于 1 和 64 之间")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getHash() {
		return hash;
	}

	public void setHash(Integer hash) {
		this.hash = hash;
	}

	public Integer getForceLock() {
		return forceLock;
	}

	public void setForceLock(Integer forceLock) {
		this.forceLock = forceLock;
	}

	public String getOpSignText() {
		return opSignText;
	}

	public void setOpSignText(String opSignText) {
		this.opSignText = opSignText;
	}
	
}