package com.thinkgem.jeesite.modules.sys.service;

import java.util.concurrent.atomic.AtomicBoolean;

import com.thinkgem.jeesite.common.config.Global;

public class MailConfig {

	private static MailConfig instance = new MailConfig();
	
	private AtomicBoolean initTag = new AtomicBoolean(false);
	
	public static MailConfig getInstance() {
		return instance ;
	}
	
	private MailConfig() {
	}
	
	public MailConfig init() {
		if(initTag.compareAndSet(false, true)) {
			//初始化
			this.setAlias(Global.getConfig("mail.alias"));
			this.setAuth(Boolean.valueOf(Global.getConfig("mail.auth")));
			this.setHost(Global.getConfig("mail.host"));
			this.setOpenssl(Boolean.valueOf(Global.getConfig("mail.openssl")));
			this.setPassword(Global.getConfig("mail.password"));
			this.setPort(Integer.parseInt(Global.getConfig("mail.port")));
			this.setSendname(Global.getConfig("mail.sendname"));
			this.setUsername(Global.getConfig("mail.username"));
		}
		return this ;
	}

	private String password;

	private String username;

	private String alias;

	private String sendname ;
	
	private Boolean auth;

	private String host;

	private Boolean openssl;

	private Integer port;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getSendname() {
		return sendname;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	public Boolean getAuth() {
		return auth;
	}

	public void setAuth(Boolean auth) {
		this.auth = auth;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Boolean getOpenssl() {
		return openssl;
	}

	public void setOpenssl(Boolean openssl) {
		this.openssl = openssl;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
	
}
