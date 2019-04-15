package com.biao.pojo;

import java.io.Serializable;

public class CardStatuScanCheckDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String contryCode; 
	private Integer cardLevel; 
	private Integer cardStatus; 
	private Integer cardStatusCheckTime;
	
	private String userId;
	private String verifyKey;
	private String appKey;
	private String appSecret;
	
	
	public String getContryCode() {
		return contryCode;
	}
	public void setContryCode(String contryCode) {
		this.contryCode = contryCode;
	}
	public Integer getCardLevel() {
		return cardLevel;
	}
	public void setCardLevel(Integer cardLevel) {
		this.cardLevel = cardLevel;
	}
	public Integer getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(Integer cardStatus) {
		this.cardStatus = cardStatus;
	}
	public Integer getCardStatusCheckTime() {
		return cardStatusCheckTime;
	}
	public void setCardStatusCheckTime(Integer cardStatusCheckTime) {
		this.cardStatusCheckTime = cardStatusCheckTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getVerifyKey() {
		return verifyKey;
	}
	public void setVerifyKey(String verifyKey) {
		this.verifyKey = verifyKey;
	}
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
	
}
