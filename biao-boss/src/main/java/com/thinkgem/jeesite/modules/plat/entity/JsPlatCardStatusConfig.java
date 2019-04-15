/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 实名认证限制Entity
 * @author ruoyu
 * @version 2018-11-27
 */
public class JsPlatCardStatusConfig extends DataEntity<JsPlatCardStatusConfig> {
	
	private static final long serialVersionUID = 1L;
	private String value;		// value
	private String label;		// label
	private String limitOut;		// limit_out
	
	public JsPlatCardStatusConfig() {
		super();
	}

	public JsPlatCardStatusConfig(String id){
		super(id);
	}

	@Length(min=1, max=1, message="value长度必须介于 1 和 1 之间")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Length(min=1, max=225, message="label长度必须介于 1 和 225 之间")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Length(min=1, max=225, message="limit_out长度必须介于 1 和 225 之间")
	public String getLimitOut() {
		return limitOut;
	}

	public void setLimitOut(String limitOut) {
		this.limitOut = limitOut;
	}
	
}