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
public class AddressConfig extends DataEntity<AddressConfig> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// name
	private String status;		// status
	
	public AddressConfig() {
		super();
	}

	public AddressConfig(String id){
		super(id);
	}

	@Length(min=0, max=16, message="name长度必须介于 0 和 16 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=1, message="status长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}