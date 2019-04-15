package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.time.LocalDateTime;

@SqlTable("email_send_log")
public class EmailSendLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @SqlField("email")
    private String email;

    @SqlField("subject")
    private String subject;

    @SqlField("content")
    private String content;

    @SqlField("template_id")
    private String templateId;

    @SqlField("response_date")
    private LocalDateTime responseDate;

    @SqlField("business_type")
    private String businessType;

    @SqlField("del_flag")
    private String delFlag;

    @SqlField("status")
    private String status;

    @SqlField("expire_time")
    private LocalDateTime expireTime;

    @SqlField("msg")
    private String msg;

    @SqlField("remarks")
    private String remarks;
    
    private Integer sendTimes ;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

	public Integer getSendTimes() {
		return sendTimes;
	}

	public void setSendTimes(Integer sendTimes) {
		this.sendTimes = sendTimes;
	}
    
}
