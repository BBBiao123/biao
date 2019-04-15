package com.biao.mail;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import com.biao.config.MailConfig;
import com.biao.config.MailConfigInfo;
import com.biao.spring.SpringBeanFactoryContext;

public class CommonMailSender {

private static Logger logger = LoggerFactory.getLogger(CommonMailSender.class);
	
	private static volatile AtomicLong atomicLong = new AtomicLong(0) ;
	
    public static Email newEmail() {
        return new HtmlEmail();
    }

    public static void initConfig(Email email,String toEmail) throws EmailException {
        // 初始化配置
    	String indexMailSub = toEmail.substring(toEmail.indexOf("@")+1, toEmail.length());
    	MailConfig mailConfig = SpringBeanFactoryContext.findBean(MailConfig.class);
    	MailConfigInfo mailConfigInfo  = mailConfig.getMail().stream()
    			.filter(confMail->confMail.getUsername().substring(confMail.getUsername().indexOf("@")+1, confMail.getUsername().length()).equalsIgnoreCase(indexMailSub))
    			.findFirst()
    			.orElse(null) ;
    	if(mailConfigInfo == null) {
    		Integer size = mailConfig.getMail().size() ;
        	Long inc = atomicLong.getAndIncrement() ;
        	Long index = (inc%size) ;
            mailConfigInfo = mailConfig.getMail().get(index.intValue());
            logger.info("发送邮件,切换邮件配置inc:{},configSzie:{},index:{}",inc,size,index);
    	}
    	logger.info("发送邮件,邮箱email:{},使用配置邮箱configEmail:{}",toEmail,mailConfigInfo.getUsername());
        email.setHostName(mailConfigInfo.getHost());
        email.setSmtpPort(mailConfigInfo.getPort());
        Boolean auth = mailConfigInfo.getAuth();
        if (auth) {
            email.setAuthenticator(new DefaultAuthenticator(mailConfigInfo.getUsername(),
            		mailConfigInfo.getPassword()));
        }
        Boolean ssl = mailConfigInfo.getOpenssl();
        if (ssl) {
            email.setSSLOnConnect(true);
        }
        // 设置主题的字符集为UTF-8
        email.setCharset("UTF-8");
        email.setFrom(mailConfigInfo.getSendname(), mailConfigInfo.getAlias());
    }


    public static void send(SimpleMailMessage simpleMessage) throws EmailException {
        Email email = newEmail();
        initConfig(email,simpleMessage.getTo()[0]);
        email.addTo(simpleMessage.getTo());
        if (!StringUtils.isEmpty(simpleMessage.getSubject())) {
            email.setSubject(simpleMessage.getSubject());
        }
        if (email instanceof HtmlEmail) {
            ((HtmlEmail) email).setHtmlMsg(simpleMessage.getText());
        }
        email.send();
    }
}
