/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 邮件模板管理Entity
 * @author ruoyu
 * @version 2018-07-10
 */
public class EmailTemplate extends DataEntity<EmailTemplate> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 模版名称
	private String code;		// 模版编码
	private String businessType;		// 业务类型
	private String templateSubject;		// 模版主题
	private String templateContent;		// 模版内容
	private String expireTime;		// 验证码失效时间（秒）
	
	public EmailTemplate() {
		super();
	}

	public EmailTemplate(String id){
		super(id);
	}

	@Length(min=1, max=255, message="模版名称长度必须介于 1 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=255, message="模版编码长度必须介于 1 和 255 之间")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=1, max=4, message="业务类型长度必须介于 1 和 4 之间")
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	
	@Length(min=1, max=255, message="模版主题长度必须介于 1 和 255 之间")
	public String getTemplateSubject() {
		return templateSubject;
	}

	public void setTemplateSubject(String templateSubject) {
		this.templateSubject = templateSubject;
	}
	
	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}
	
	@Length(min=0, max=11, message="验证码失效时间（秒）长度必须介于 0 和 11 之间")
	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	
}