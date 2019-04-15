/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 手动转账Entity
 * @author ruoyu
 * @version 2018-08-09
 */
public class JsPlatUserCoinVolumeHistory extends DataEntity<JsPlatUserCoinVolumeHistory> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// user_id
	private String account;		// account
	private String type;		// 拨币场景
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private String volume;		// 币种数量
	private String userTag ;
	private String remark ;//备注
	
	private String changeAsset = "false" ;
	
	public JsPlatUserCoinVolumeHistory() {
		super();
	}

	public JsPlatUserCoinVolumeHistory(String id){
		super(id);
	}

	@NotNull(message="user_id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=64, message="account长度必须介于 1 和 64 之间")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@Length(min=1, max=20, message="拨币场景长度必须介于 1 和 20 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=1, max=64, message="币种id长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=45, message="币种代号长度必须介于 1 和 45 之间")
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

	public String getUserTag() {
		return userTag;
	}

	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getChangeAsset() {
		return changeAsset;
	}

	public void setChangeAsset(String changeAsset) {
		this.changeAsset = changeAsset;
	}
	
}