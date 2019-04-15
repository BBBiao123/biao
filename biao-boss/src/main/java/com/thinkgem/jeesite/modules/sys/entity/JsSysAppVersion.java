/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * App版本管理Entity
 * @author zzj
 * @version 2018-08-27
 */
public class JsSysAppVersion extends DataEntity<JsSysAppVersion> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// APP类型
	private String version;		// 版本号
	private String address;		// 下载地址
	private String isUpgrade;   //是否强制更新
	
	public JsSysAppVersion() {
		super();
	}

	public JsSysAppVersion(String id){
		super(id);
	}

	@Length(min=0, max=1, message="APP类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=20, message="版本号长度必须介于 0 和 20 之间")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Length(min=0, max=200, message="下载地址长度必须介于 0 和 200 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsUpgrade() {
		return isUpgrade;
	}

	public void setIsUpgrade(String isUpgrade) {
		this.isUpgrade = isUpgrade;
	}
}