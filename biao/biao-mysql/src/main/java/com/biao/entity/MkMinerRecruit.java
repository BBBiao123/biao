package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

@SqlTable("mk_miner_recruit")
public class MkMinerRecruit extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("mail")
    private String mail;
    @SqlField("mobile")
    private String mobile;
    @SqlField("real_name")
    private String realName;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("is_standard")
    private String isStandard;
    @SqlField("invite_number")
    private Integer inviteNumber;
    @SqlField("reach_number")
    private Integer reachNumber;
    @SqlField("remark")
    private String remark;

    private String isInviteNumber;
    private String isReachNumber;

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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getIsStandard() {
        return isStandard;
    }

    public void setIsStandard(String isStandard) {
        this.isStandard = isStandard;
    }

    public Integer getInviteNumber() {
        return inviteNumber;
    }

    public void setInviteNumber(Integer inviteNumber) {
        this.inviteNumber = inviteNumber;
    }

    public Integer getReachNumber() {
        return reachNumber;
    }

    public void setReachNumber(Integer reachNumber) {
        this.reachNumber = reachNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsInviteNumber() {
        return isInviteNumber;
    }

    public void setIsInviteNumber(String isInviteNumber) {
        this.isInviteNumber = isInviteNumber;
    }

    public String getIsReachNumber() {
        return isReachNumber;
    }

    public void setIsReachNumber(String isReachNumber) {
        this.isReachNumber = isReachNumber;
    }
}
