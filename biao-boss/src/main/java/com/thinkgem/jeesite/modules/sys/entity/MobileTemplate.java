/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * ddEntity
 * @author ruoyu
 * @version 2018-06-28
 */
public class MobileTemplate extends DataEntity<MobileTemplate> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 业务类型
	private String accessKey;		// 短信key
	private String accessSecret;		// 短信secret
	private String signName;		// 短信签名
	private String templateParam;		// 模板参数
	private String templateCode;		// 模板id
	private String workSign;		// 业务标识
	private String remark;		// 备注描述
	private String expandJson;		// 扩展字段
	private String timeOut;		// 超时时间
	
	public MobileTemplate() {
		super();
	}

	public MobileTemplate(String id){
		super(id);
	}

	@Length(min=1, max=20, message="业务类型长度必须介于 1 和 20 之间")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=1, max=100, message="短信key长度必须介于 1 和 100 之间")
	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	@Length(min=1, max=100, message="短信secret长度必须介于 1 和 100 之间")
	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
	
	@Length(min=0, max=50, message="短信签名长度必须介于 0 和 50 之间")
	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}
	
	@Length(min=1, max=255, message="模板参数长度必须介于 1 和 255 之间")
	public String getTemplateParam() {
		return templateParam;
	}

	public void setTemplateParam(String templateParam) {
		this.templateParam = templateParam;
	}
	
	@Length(min=1, max=100, message="模板id长度必须介于 1 和 100 之间")
	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	@Length(min=0, max=255, message="业务标识长度必须介于 0 和 255 之间")
	public String getWorkSign() {
		return workSign;
	}

	public void setWorkSign(String workSign) {
		this.workSign = workSign;
	}
	
	@Length(min=0, max=255, message="备注描述长度必须介于 0 和 255 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Length(min=0, max=255, message="扩展字段长度必须介于 0 和 255 之间")
	public String getExpandJson() {
		return expandJson;
	}

	public void setExpandJson(String expandJson) {
		this.expandJson = expandJson;
	}
	
	@Length(min=0, max=11, message="超时时间长度必须介于 0 和 11 之间")
	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	
}