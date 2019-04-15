/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 邮件发送日志管理Entity
 * @author ruoyu
 * @version 2018-07-10
 */
public class EmailSendLog extends DataEntity<EmailSendLog> {
	
	private static final long serialVersionUID = 1L;
	private String email;		// 联系电话
	private String subject;		// 主题
	private String content;		// 发送数据
	private String templateId;		// 模板code
	private Date responseDate;		// 响应时间
	private String businessType;		// 业务类型
	private String status;		// 发送状态
	private String msg;		// 返回消息
	private Date expireTime;		// 失效时间
	
	public EmailSendLog() {
		super();
	}

	public EmailSendLog(String id){
		super(id);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=255, message="主题长度必须介于 0 和 255 之间")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=255, message="模板code长度必须介于 0 和 255 之间")
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}
	
	@Length(min=0, max=20, message="业务类型长度必须介于 0 和 20 之间")
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	
	@Length(min=1, max=4, message="发送状态长度必须介于 1 和 4 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=40, message="返回消息长度必须介于 0 和 40 之间")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
}