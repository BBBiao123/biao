/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.robot.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 价格机器人Entity
 * @author dazi
 * @version 2018-06-06
 */
public class RobotConfig extends DataEntity<RobotConfig> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// type
	private String coinMain;		// coin_main
	private String coinOther;		// coin_other
	private User user;		// user_id
	private String volumeRange;		// volume_range
	private String priceRange;		// price_range
	private byte status ;//状态  0:启用 1:禁用
	
	public RobotConfig() {
		super();
	}

	public RobotConfig(String id){
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

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
}