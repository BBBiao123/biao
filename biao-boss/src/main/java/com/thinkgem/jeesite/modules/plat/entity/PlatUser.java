/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 前台用户Entity
 *
 * @author dazi
 * @version 2018-04-26
 */
public class PlatUser extends DataEntity<PlatUser> {

    private static final long serialVersionUID = 1L;
    private String username;        // 邮箱
    private String password;        // 密码
    private String userType;        // 用户类型
    private String status;        // 是否锁定
    private String mobile;        // mobile
    private String mail;        // 邮箱
    private String exPassword;        // 交易密码
    private String googleAuth;        // Google认证
    private String sex;        // 0:未知 1：男 2：女
    private String age;        // age
    private String inviteCode;        // 邀请码
    private String realName;        // 真实姓名
    private String idCard;        // 身份证
    private String cardUpId;        // 身份证 正面图案id
    private String cardDownId;        // 身份证反面id
    private String cardFaceId;        // 手持身份id
    private String cardStatus;        // 身份证审核状态 -1 ：不通过 1：通过
    private String cardStatusReason;        // card_status_reason
    private String openDiscount;        // 是否开启平台币抵扣手续费
    private String countryId;        // country_id
    private String countryCode;        // country_code
    private String wechatNo;        // 微信号
    private String wechatQrcodeId;        // 微信收款码
    private String alipayNo;        // 支付宝账号
    private String alipayQrcodeId;        // 支付宝收款码
    private String referId;        // 推荐人
    private String referInviteCode;        // 推荐人邀请码
    private String c2cIn;
    private String c2cOut;
    private String coinOut;
    private String c2cPublish;
    private String source;
    private String tag ;
    private String c2cChange; //c2c转账
    private String c2cSwitch; //c2c交易开关
    private String tradeSwitch; //币币交易开关
    private String reason; //原因
    private Date lockDate;
    private String isLockTrade;
    
    private Integer cardLevel ;
    private Integer cardStatusCheckTime ;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Length(min = 0, max = 1, message = "c2cIn长度必须介于 0 和 1 之间")
    public String getC2cIn() {
        return c2cIn;
    }

    public void setC2cIn(String c2cIn) {
        this.c2cIn = c2cIn;
    }

    @Length(min = 0, max = 1, message = "coinOut长度必须介于 0 和 1 之间")
    public String getCoinOut() {
        return coinOut;
    }

    public void setCoinOut(String coinOut) {
        this.coinOut = coinOut;
    }

    @Length(min = 0, max = 1, message = "c2cPublish长度必须介于 0 和 1 之间")
    public String getC2cPublish() {
        return c2cPublish;
    }

    public void setC2cPublish(String c2cPublish) {
        this.c2cPublish = c2cPublish;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditDate;// 审核通过时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public PlatUser() {
        super();
    }

    public PlatUser(String id) {
        super(id);
    }

    @Length(min = 1, max = 45, message = "邮箱长度必须介于 1 和 45 之间")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Length(min = 1, max = 100, message = "密码长度必须介于 1 和 100 之间")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Length(min = 0, max = 1, message = "用户类型长度必须介于 0 和 1 之间")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Length(min = 0, max = 1, message = "是否锁定长度必须介于 0 和 1 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Length(min = 0, max = 11, message = "mobile长度必须介于 0 和 11 之间")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Length(min = 0, max = 64, message = "邮箱长度必须介于 0 和 20 之间")
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getExPassword() {
        return exPassword;
    }

    public void setExPassword(String exPassword) {
        this.exPassword = exPassword;
    }

    @Length(min = 0, max = 45, message = "Google认证长度必须介于 0 和 45 之间")
    public String getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(String googleAuth) {
        this.googleAuth = googleAuth;
    }

    @Length(min = 0, max = 2, message = "0:未知 1：男 2：女长度必须介于 0 和 2 之间")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Length(min = 0, max = 2, message = "age长度必须介于 0 和 2 之间")
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Length(min = 0, max = 30, message = "邀请码长度必须介于 0 和 30 之间")
    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Length(min = 0, max = 45, message = "真实姓名长度必须介于 0 和 45 之间")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Length(min = 0, max = 18, message = "身份证长度必须介于 0 和 18 之间")
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Length(min = 0, max = 128, message = "身份证 正面图案id长度必须介于 0 和 128 之间")
    public String getCardUpId() {
        return cardUpId;
    }

    public void setCardUpId(String cardUpId) {
        this.cardUpId = cardUpId;
    }

    @Length(min = 0, max = 128, message = "身份证反面id长度必须介于 0 和 128 之间")
    public String getCardDownId() {
        return cardDownId;
    }

    public void setCardDownId(String cardDownId) {
        this.cardDownId = cardDownId;
    }

    @Length(min = 0, max = 128, message = "手持身份id长度必须介于 0 和 128 之间")
    public String getCardFaceId() {
        return cardFaceId;
    }

    public void setCardFaceId(String cardFaceId) {
        this.cardFaceId = cardFaceId;
    }

    @Length(min = 0, max = 100, message = "身份证审核状态 -1 ：不通过 100：通过长度必须介于 0 和 100 之间")
    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    @Length(min = 0, max = 128, message = "card_status_reason长度必须介于 0 和 128 之间")
    public String getCardStatusReason() {
        return cardStatusReason;
    }

    public void setCardStatusReason(String cardStatusReason) {
        this.cardStatusReason = cardStatusReason;
    }

    @Length(min = 0, max = 1, message = "是否开启平台币抵扣手续费长度必须介于 0 和 1 之间")
    public String getOpenDiscount() {
        return openDiscount;
    }

    public void setOpenDiscount(String openDiscount) {
        this.openDiscount = openDiscount;
    }

    @Length(min = 0, max = 64, message = "country_id长度必须介于 0 和 64 之间")
    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Length(min = 0, max = 10, message = "country_code长度必须介于 0 和 10 之间")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Length(min = 0, max = 24, message = "微信号长度必须介于 0 和 24 之间")
    public String getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(String wechatNo) {
        this.wechatNo = wechatNo;
    }

    @Length(min = 0, max = 128, message = "微信收款码长度必须介于 0 和 128 之间")
    public String getWechatQrcodeId() {
        return wechatQrcodeId;
    }

    public void setWechatQrcodeId(String wechatQrcodeId) {
        this.wechatQrcodeId = wechatQrcodeId;
    }

    @Length(min = 0, max = 255, message = "支付宝账号长度必须介于 0 和 255 之间")
    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    @Length(min = 0, max = 255, message = "支付宝收款码长度必须介于 0 和 255 之间")
    public String getAlipayQrcodeId() {
        return alipayQrcodeId;
    }

    public void setAlipayQrcodeId(String alipayQrcodeId) {
        this.alipayQrcodeId = alipayQrcodeId;
    }

    @Length(min = 0, max = 64, message = "推荐人长度必须介于 0 和 64 之间")
    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getReferInviteCode() {
        return referInviteCode;
    }

    public void setReferInviteCode(String referInviteCode) {
        this.referInviteCode = referInviteCode;
    }

    public String getC2cOut() {
        return c2cOut;
    }

    public void setC2cOut(String c2cOut) {
        this.c2cOut = c2cOut;
    }

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

    public String getC2cChange() {
        return c2cChange;
    }

    public void setC2cChange(String c2cChange) {
        this.c2cChange = c2cChange;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getC2cSwitch() {
        return c2cSwitch;
    }

    public void setC2cSwitch(String c2cSwitch) {
        this.c2cSwitch = c2cSwitch;
    }

    public String getTradeSwitch() {
        return tradeSwitch;
    }

    public void setTradeSwitch(String tradeSwitch) {
        this.tradeSwitch = tradeSwitch;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public void setLockDate(Date lockDate) {
        this.lockDate = lockDate;
    }

    public String getIsLockTrade() {
        return isLockTrade;
    }

    public void setIsLockTrade(String isLockTrade) {
        this.isLockTrade = isLockTrade;
    }

	public Integer getCardLevel() {
		return cardLevel;
	}

	public void setCardLevel(Integer cardLevel) {
		this.cardLevel = cardLevel;
	}

	public Integer getCardStatusCheckTime() {
		return cardStatusCheckTime;
	}

	public void setCardStatusCheckTime(Integer cardStatusCheckTime) {
		this.cardStatusCheckTime = cardStatusCheckTime;
	}
    
}