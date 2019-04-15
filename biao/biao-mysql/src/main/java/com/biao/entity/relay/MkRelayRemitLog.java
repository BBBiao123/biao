package com.biao.entity.relay;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

@SqlTable("mk_relay_remit_log")
public class MkRelayRemitLog extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("mail")
    private String mail;
    @SqlField("mobile")
    private String mobile;
    @SqlField("real_name")
    private String realName;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("user_type")
    private String userType;
    @SqlField("is_remit")
    private String isRemit;
    @SqlField("referee_id")
    private String refereeId;
    @SqlField("refer_mail")
    private String referMail;
    @SqlField("refer_mobile")
    private String referMobile;
    @SqlField("refer_real_name")
    private String referRealName;
    @SqlField("remark")
    private String remark;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIsRemit() {
        return isRemit;
    }

    public void setIsRemit(String isRemit) {
        this.isRemit = isRemit;
    }

    public String getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(String refereeId) {
        this.refereeId = refereeId;
    }

    public String getReferMail() {
        return referMail;
    }

    public void setReferMail(String referMail) {
        this.referMail = referMail;
    }

    public String getReferMobile() {
        return referMobile;
    }

    public void setReferMobile(String referMobile) {
        this.referMobile = referMobile;
    }

    public String getReferRealName() {
        return referRealName;
    }

    public void setReferRealName(String referRealName) {
        this.referRealName = referRealName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
