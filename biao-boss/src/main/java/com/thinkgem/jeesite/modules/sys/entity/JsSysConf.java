/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * Plat系统配置Entity
 * @author zzj
 * @version 2019-03-05
 */
public class JsSysConf extends DataEntity<JsSysConf> {
	
	private static final long serialVersionUID = 1L;
	private String offlineOnOff;		// c2c开关
	
	public JsSysConf() {
		super();
	}

	public JsSysConf(String id){
		super(id);
	}

	@Length(min=1, max=1, message="c2c开关长度必须介于 1 和 1 之间")
	public String getOfflineOnOff() {
		return offlineOnOff;
	}

	public void setOfflineOnOff(String offlineOnOff) {
		this.offlineOnOff = offlineOnOff;
	}
	
}