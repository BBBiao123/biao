/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * bb to c2c转账日志Entity
 * @author ruoyu
 * @version 2018-08-28
 */
public class JsPlatOfflineTransferLog extends DataEntity<JsPlatOfflineTransferLog> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private String coinId;		// 币id
	private String coinSymbol;		// 币符号
	private String volume;		// volume
	private String sourceVolume; //
	private String type;		// 0:转入 1：转出
	private String userId;
	private String mark;//

	public JsPlatOfflineTransferLog() {
		super();
	}

	public JsPlatOfflineTransferLog(String id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=64, message="币id长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=64, message="币符号长度必须介于 1 和 64 之间")
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
	
	@Length(min=0, max=1, message="0:转入 1：转出长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getSourceVolume() {
		return sourceVolume;
	}

	public void setSourceVolume(String sourceVolume) {
		this.sourceVolume = sourceVolume;
	}
}