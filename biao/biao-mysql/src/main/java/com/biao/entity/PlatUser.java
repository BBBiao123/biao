package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.time.LocalDateTime;

@SqlTable("js_plat_user")
public class PlatUser extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("username")
    private String username;
    @SqlField("password")
    private String password;
    @SqlField("user_type")
    private Integer userType;
    @SqlField("status")
    private Integer status;
    @SqlField("mobile")
    private String mobile;
    @SqlField("mail")
    private String mail;
    @SqlField("ex_password")
    private String exPassword;
    @SqlField("google_auth")
    private String googleAuth;
    @SqlField("sex")
    private Integer sex;
    @SqlField("age")
    private Integer age;
    @SqlField("real_name")
    private String realName;
    @SqlField("id_card")
    private String idCard;
    @SqlField("card_up_id")
    private String cardUpId;
    @SqlField("card_down_id")
    private String cardDownId;
    @SqlField("card_face_id")
    private String cardFaceId;
    
    @SqlField("card_status")
    private Integer cardStatus;
    
    @SqlField("card_status_reason")
    private String cardStatusReason;
    
    @SqlField("card_status_check_time")
    private Integer cardStatusCheckTime ;
    @SqlField("card_level")
    private Integer cardLevel ;
    
    @SqlField("nick_name")
    private String nickName ;
    
    @SqlField("receive_msg")
    private Integer receiveMsg ;
    
    @SqlField("open_discount")
    private Integer openDiscount;//是否开启平台币抵扣手续费
    @SqlField("remarks")
    private String remarks;
    @SqlField("country_id")
    private String countryId;
    @SqlField("country_code")
    private String countryCode;
    @SqlField("invite_code")
    private String inviteCode;
    @SqlField("refer_id")
    private String referId;
    @SqlField("lock_date")
    private LocalDateTime lockDate;

    private String oldPassword;


    //重置密码token
    private String ptoken;
    @SqlField("alipay_no")
    private String alipayNo;

    @SqlField("alipay_qrcode_id")
    private String alipayQrcodeId;

    @SqlField("wechat_qrcode_id")
    private String wechatQrcodeId;

    @SqlField("wechat_no")
    private String wechatNo;

    @SqlField("refer_invite_code")
    private String referInviteCode;

    @SqlField("mobile_audit_date")
    private LocalDateTime mobileAuditDate;

    @SqlField("c2c_in")
    private String c2cIn;

    @SqlField("c2c_out")
    private String c2cOut;

    @SqlField("c2c_publish")
    private String c2cPublish;

    @SqlField("coin_out")
    private String coinOut;

    @SqlField("source")
    private String source;

    @SqlField("is_award")
    private String isAward;

    @SqlField("ex_valid_type")
    private Integer exValidType;

    @SqlField("tag")
    private String tag;

    @SqlField("c2c_change")
    private String c2cChange;

    @SqlField("is_registered_cs")
    private String isRegisteredCs;

    private String multielementTag;

    private String moneyHoldingTag;


    public String getC2cIn() {
        return c2cIn;
    }

    public void setC2cIn(String c2cIn) {
        this.c2cIn = c2cIn;
    }

    public String getCoinOut() {
        return coinOut;
    }

    public void setCoinOut(String coinOut) {
        this.coinOut = coinOut;
    }

    public String getC2cPublish() {
        return c2cPublish;
    }

    public void setC2cPublish(String c2cPublish) {
        this.c2cPublish = c2cPublish;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getExPassword() {
        return exPassword;
    }

    public void setExPassword(String exPassword) {
        this.exPassword = exPassword;
    }

    public String getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(String googleAuth) {
        this.googleAuth = googleAuth;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardStatusReason() {
        return cardStatusReason;
    }

    public void setCardStatusReason(String cardStatusReason) {
        this.cardStatusReason = cardStatusReason;
    }

    public Integer getOpenDiscount() {
        return openDiscount;
    }

    public void setOpenDiscount(Integer openDiscount) {
        this.openDiscount = openDiscount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
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

    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }

    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    public String getAlipayQrcodeId() {
        return alipayQrcodeId;
    }

    public void setAlipayQrcodeId(String alipayQrcodeId) {
        this.alipayQrcodeId = alipayQrcodeId;
    }

    public String getWechatQrcodeId() {
        return wechatQrcodeId;
    }

    public void setWechatQrcodeId(String wechatQrcodeId) {
        this.wechatQrcodeId = wechatQrcodeId;
    }

    public String getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(String wechatNo) {
        this.wechatNo = wechatNo;
    }

    public String getReferInviteCode() {
        return referInviteCode;
    }

    public void setReferInviteCode(String referInviteCode) {
        this.referInviteCode = referInviteCode;
    }

    public LocalDateTime getMobileAuditDate() {
        return mobileAuditDate;
    }

    public void setMobileAuditDate(LocalDateTime mobileAuditDate) {
        this.mobileAuditDate = mobileAuditDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIsAward() {
        return isAward;
    }

    public void setIsAward(String isAward) {
        this.isAward = isAward;
    }

    public String getC2cOut() {
        return c2cOut;
    }

    public void setC2cOut(String c2cOut) {
        this.c2cOut = c2cOut;
    }

    public Integer getExValidType() {
        return exValidType;
    }

    public void setExValidType(Integer exValidType) {
        this.exValidType = exValidType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMultielementTag() {
        return multielementTag;
    }

    public void setMultielementTag(String multielementTag) {
        this.multielementTag = multielementTag;
    }

    public String getMoneyHoldingTag() {
        return moneyHoldingTag;
    }

    public void setMoneyHoldingTag(String moneyHoldingTag) {
        this.moneyHoldingTag = moneyHoldingTag;
    }

    public String getC2cChange() {
        return c2cChange;
    }

    public void setC2cChange(String c2cChange) {
        this.c2cChange = c2cChange;
    }

    public LocalDateTime getLockDate() {
        return lockDate;
    }

    public void setLockDate(LocalDateTime lockDate) {
        this.lockDate = lockDate;
    }

	public Integer getCardStatusCheckTime() {
		return cardStatusCheckTime;
	}

	public void setCardStatusCheckTime(Integer cardStatusCheckTime) {
		this.cardStatusCheckTime = cardStatusCheckTime;
	}

	public Integer getCardLevel() {
		return cardLevel;
	}

	public void setCardLevel(Integer cardLevel) {
		this.cardLevel = cardLevel;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getReceiveMsg() {
		return receiveMsg;
	}

	public void setReceiveMsg(Integer receiveMsg) {
		this.receiveMsg = receiveMsg;
	}

    public String getIsRegisteredCs() {
        return isRegisteredCs;
    }

    public void setIsRegisteredCs(String isRegisteredCs) {
        this.isRegisteredCs = isRegisteredCs;
    }
}
