package com.biao.pojo;

import java.io.Serializable;

public class SmsMessageDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String mobile ;
	
	private String templateCode ;
	
	private String outId ;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}
	
}
