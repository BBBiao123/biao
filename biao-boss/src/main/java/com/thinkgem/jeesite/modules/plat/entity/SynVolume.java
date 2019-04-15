package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class SynVolume extends DataEntity<ExPair>{

	private static final long serialVersionUID = 1L;

	private String userId ;
	
	private String email ;
	
	private String mobile ;
	
	private String coinId ;
	
	private String symbol ;
	
	private String volume ;
	
	private String lockVolume ;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	public static String getKey(String userId, String symbol) {
        return "lock_user_coin_" + userId + symbol;
    }

	public String getLockVolume() {
		return lockVolume;
	}

	public void setLockVolume(String lockVolume) {
		this.lockVolume = lockVolume;
	}
	
}
