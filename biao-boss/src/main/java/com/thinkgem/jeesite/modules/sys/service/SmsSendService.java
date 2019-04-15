package com.thinkgem.jeesite.modules.sys.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import com.thinkgem.jeesite.common.utils.JedisUtils;


public class SmsSendService {

	private static Logger logger = LoggerFactory.getLogger(SmsSendService.class);
	/**
	 * 发送验证码
	 * 
	 * @param eamil
	 * @param code
	 */
	public static void sendEmail(String username,String eamil, String code,boolean setRedis) {
		if (StringUtils.isBlank(code)) {
			code = NumberUtils.getRandomNumber(6);
		}
		String content = "你本次的登录验证码为:" + code;
		SimpleMailMessage simpleMessage = new SimpleMailMessage();
		simpleMessage.setText(content);
		simpleMessage.setTo(eamil);
		simpleMessage.setFrom("");
		simpleMessage.setSubject("boss登录验证码");
		try {
			CommonMailSender.send(simpleMessage);
			logger.info("用户邮箱email={},本次登录发送的邮箱验证码为code:{}",eamil,code);
			if(setRedis) {
				String redisKey = SmsSendService.buildValidRedisKey(username);
				JedisUtils.set(redisKey, code, 300);
			}
		} catch (EmailException e) {
			logger.error("发送boss登录邮箱email:{},发送异常e:{}",eamil,e.getMessage());
		}
	}
	
	/**
	 * 发送验证码
	 * 
	 * @param eamil
	 * @param code
	 */
	public static void sendEmail(String username,String eamil, String code,String sendIp,boolean setRedis) {
		if (StringUtils.isBlank(code)) {
			code = NumberUtils.getRandomNumber(6);
		}
		String content = "验证码为:" + code+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;你本次的登录ip:"+sendIp;
		SimpleMailMessage simpleMessage = new SimpleMailMessage();
		simpleMessage.setText(content);
		simpleMessage.setTo(eamil);
		simpleMessage.setFrom("");
		simpleMessage.setSubject("boss登录验证码");
		try {
			CommonMailSender.send(simpleMessage);
			logger.info("用户邮箱email={},本次登录发送的邮箱验证码为code:{}",eamil,code);
			if(setRedis) {
				String redisKey = SmsSendService.buildValidRedisKey(username);
				JedisUtils.set(redisKey, code, 300);
			}
		} catch (EmailException e) {
			logger.error("发送boss登录邮箱email:{},发送异常e:{}",eamil,e.getMessage());
		}
	}
	
	public static void setRedisCode(String username,String code) {
		String redisKey = SmsSendService.buildValidRedisKey(username);
		JedisUtils.set(redisKey, code, 300);
	}

	/**
	 * 验证邮箱验证码
	 * 
	 * @param eamil
	 * @param code
	 * @return
	 */
	public static boolean validEmailCode(String username, String code) {
		String redisKey = SmsSendService.buildValidRedisKey(username);
		String redisCode = JedisUtils.get(redisKey);
		if(StringUtils.isNotBlank(redisCode)&&redisCode.equalsIgnoreCase(code)) {
			JedisUtils.del(redisKey);
			return true ;
		}
		return false;
	}
	
	private static String buildValidRedisKey(String username) {
		return "boss-login:"+username ;
	}
}
