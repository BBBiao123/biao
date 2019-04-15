/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 币安自动化管理Entity
 * @author xiaoyu
 * @version 2018-12-25
 */
public class RobotConfigUnsafe extends DataEntity<RobotConfigUnsafe> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// type
	private String coinMain;		// coin_main
	private String coinOther;		// coin_other
	private User user;		// user_id
	private String userName;		// user_name
	private String password;		// password
	private String volumeRange;		// volume_range
	private String priceRange;		// price_range
	private String status;		// 状态  0:启用 1:禁用
	private String isInit;		// is_init
	
	public RobotConfigUnsafe() {
		super();
	}

	public RobotConfigUnsafe(String id){
		super(id);
	}

	@Length(min=0, max=11, message="type长度必须介于 0 和 11 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=16, message="coin_main长度必须介于 0 和 16 之间")
	public String getCoinMain() {
		return coinMain;
	}

	public void setCoinMain(String coinMain) {
		this.coinMain = coinMain;
	}
	
	@Length(min=0, max=16, message="coin_other长度必须介于 0 和 16 之间")
	public String getCoinOther() {
		return coinOther;
	}

	public void setCoinOther(String coinOther) {
		this.coinOther = coinOther;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=100, message="user_name长度必须介于 0 和 100 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=0, max=1000, message="password长度必须介于 0 和 1000 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Length(min=0, max=100, message="volume_range长度必须介于 0 和 100 之间")
	public String getVolumeRange() {
		return volumeRange;
	}

	public void setVolumeRange(String volumeRange) {
		this.volumeRange = volumeRange;
	}
	
	@Length(min=0, max=100, message="price_range长度必须介于 0 和 100 之间")
	public String getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}
	
	@Length(min=0, max=1, message="状态  0:启用 1:禁用长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=1, message="is_init长度必须介于 0 和 1 之间")
	public String getIsInit() {
		return isInit;
	}

	public void setIsInit(String isInit) {
		this.isInit = isInit;
	}
	
}