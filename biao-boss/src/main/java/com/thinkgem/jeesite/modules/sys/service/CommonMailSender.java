package com.thinkgem.jeesite.modules.sys.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.mail.SimpleMailMessage;


public class CommonMailSender {

	public static Email newEmail() {
		return new HtmlEmail();
	}
	
	public static void initConfig(Email email) throws EmailException {
		// 初始化配置
		MailConfig mailConfig = MailConfig.getInstance().init();
		email.setHostName(mailConfig.getHost());
		email.setSmtpPort(mailConfig.getPort());
		Boolean auth = mailConfig.getAuth();
		if (auth) {
			email.setAuthenticator(new DefaultAuthenticator(mailConfig.getUsername(),
					mailConfig.getPassword()));
		}
		Boolean ssl = mailConfig.getOpenssl();
		if (ssl) {
			email.setSSLOnConnect(true);
		}
		// 设置主题的字符集为UTF-8
		email.setCharset("UTF-8");
		email.setFrom(mailConfig.getSendname(),mailConfig.getAlias());
	}
	
	public static void send(SimpleMailMessage simpleMessage) throws EmailException{
		Email email = newEmail() ;
		initConfig(email);
		email.addTo(simpleMessage.getTo());
		if (!StringUtils.isEmpty(simpleMessage.getSubject())) {
			email.setSubject(simpleMessage.getSubject());
		}
		if(email instanceof HtmlEmail) {
			((HtmlEmail)email).setHtmlMsg(simpleMessage.getText());
		}
		email.send();
	}
}
