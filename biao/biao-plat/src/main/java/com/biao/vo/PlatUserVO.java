package com.biao.vo;

import java.io.Serializable;

public class PlatUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    //注册类型
    private Integer registerType;

    //国籍
    private String countryId;

    //国家区号
    private String countryCode;

    private String mobile;

    private String mail;

    //验证码
    private String code;

    //短信验证码
    private String smsCode;

    //邀请码
    private String inviteCode;

    private String password;

    private String cardUpId;
    private String cardDownId;
    private String cardFaceId;
    private String realName;
    private String idCard;
    private Integer age;
    private Integer sex;//1:男  2:女
    private String address;

    //重置密码使用的token
    private String ptoken;
    /**
     * 原始密码
     */
    private String oldPassword;

    private String secret;
    
    private String nickName ;

    /**
     * 交易密码
     */
    private String exPassword;
    private Integer exValidType;
    private String smsType;

    private String referInviteCode;

    private String vaildCodeKey;

    public Integer getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Integer registerType) {
        this.registerType = registerType;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCardUpId() {
        return cardUpId;
    }

    public void setCardUpId(String cardUpId) {
        this.cardUpId = cardUpId;
    }

    public String getCardDownId() {
        return cardDownId;
    }

    public void setCardDownId(String cardDownId) {
        this.cardDownId = cardDownId;
    }

    public String getCardFaceId() {
        return cardFaceId;
    }

    public void setCardFaceId(String cardFaceId) {
        this.cardFaceId = cardFaceId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPtoken() {
        return ptoken;
    }

    public void setPtoken(String ptoken) {
        this.ptoken = ptoken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExPassword() {
        return exPassword;
    }

    public void setExPassword(String exPassword) {
        this.exPassword = exPassword;
    }

    public Integer getExValidType() {
        return exValidType;
    }

    public void setExValidType(Integer exValidType) {
        this.exValidType = exValidType;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

    public String getReferInviteCode() {
        return referInviteCode;
    }

    public void setReferInviteCode(String referInviteCode) {
        this.referInviteCode = referInviteCode;
    }

    public String getVaildCodeKey() {
        return vaildCodeKey;
    }

    public void setVaildCodeKey(String vaildCodeKey) {
        this.vaildCodeKey = vaildCodeKey;
    }
}
