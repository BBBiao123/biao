/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * eth_token的volumeEntity
 * @author ruoyu
 * @version 2018-07-03
 */
public class EthTokenVolume extends DataEntity<EthTokenVolume> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// name
	private String volume;		// volume
	private String address;		// address
	
	public EthTokenVolume() {
		super();
	}

	public EthTokenVolume(String id){
		super(id);
	}

	@Length(min=1, max=16, message="name长度必须介于 1 和 16 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	@Length(min=0, max=100, message="address长度必须介于 0 和 100 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}