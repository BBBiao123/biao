package com.biao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aliyun.cardcheck")
public class AliYunCardCheckConfig {

	private String appKey ;
	private String appSecret ;
	private String verifyKey ;
	private String userId ;
	private Integer maxCheckTime ;
	
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getVerifyKey() {
		return verifyKey;
	}
	public void setVerifyKey(String verifyKey) {
		this.verifyKey = verifyKey;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getMaxCheckTime() {
		return maxCheckTime;
	}
	public void setMaxCheckTime(Integer maxCheckTime) {
		this.maxCheckTime = maxCheckTime;
	}
	
}
