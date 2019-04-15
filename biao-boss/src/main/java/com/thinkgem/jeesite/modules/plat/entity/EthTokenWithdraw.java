/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * eth token withdrawEntity
 * @author ruoyu
 * @version 2018-07-03
 */
public class EthTokenWithdraw extends DataEntity<EthTokenWithdraw> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// name
	private String volume;		// volume
	private String fromAddress;		// from_address
	private String toAddress;		// to_address
	private String status;		// status
	
	public EthTokenWithdraw() {
		super();
	}

	public EthTokenWithdraw(String id){
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
	
	@Length(min=1, max=100, message="from_address长度必须介于 1 和 100 之间")
	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	
	@Length(min=1, max=100, message="to_address长度必须介于 1 和 100 之间")
	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	
	@Length(min=1, max=1, message="status长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}