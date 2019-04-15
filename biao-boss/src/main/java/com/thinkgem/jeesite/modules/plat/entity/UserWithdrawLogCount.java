package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class UserWithdrawLogCount extends DataEntity<UserWithdrawLogCount>{

	private static final long serialVersionUID = 1L;
	
	private String coinSymbol ;
	private String bbVolumn ;
	private String ccVolumn ;
	private String lockVolumn ;
	private String volumn ;
	private String sumVolumn ;
	private String plusVolumn ;
	public String getCoinSymbol() {
		return coinSymbol;
	}
	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	public String getBbVolumn() {
		return bbVolumn;
	}
	public void setBbVolumn(String bbVolumn) {
		this.bbVolumn = bbVolumn;
	}
	public String getCcVolumn() {
		return ccVolumn;
	}
	public void setCcVolumn(String ccVolumn) {
		this.ccVolumn = ccVolumn;
	}
	public String getLockVolumn() {
		return lockVolumn;
	}
	public void setLockVolumn(String lockVolumn) {
		this.lockVolumn = lockVolumn;
	}
	public String getVolumn() {
		return volumn;
	}
	public void setVolumn(String volumn) {
		this.volumn = volumn;
	}
	public String getSumVolumn() {
		return sumVolumn;
	}
	public void setSumVolumn(String sumVolumn) {
		this.sumVolumn = sumVolumn;
	}
	public String getPlusVolumn() {
		return plusVolumn;
	}
	public void setPlusVolumn(String plusVolumn) {
		this.plusVolumn = plusVolumn;
	}
	
}
