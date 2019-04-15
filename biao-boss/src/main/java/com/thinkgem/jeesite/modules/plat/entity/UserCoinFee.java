/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户交易对手续费设置Entity
 *
 * @author dapao
 * @version 2018-08-31
 */
public class UserCoinFee extends DataEntity<UserCoinFee> {

    private static final long serialVersionUID = 1L;
    private String pairOne;
    private String pairOther;
    private String userId;
    private String fee;
    private Integer status;
    private String userName;
    private String realName;
    private String mail;
    private String mobile;
    private String exPairId;

    public String getExPairId() {
        return exPairId;
    }

    public void setExPairId(String exPairId) {
        this.exPairId = exPairId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserCoinFee() {
        super();
    }

    public UserCoinFee(String id) {
        super(id);
    }

    @Length(min = 1, max = 45, message = "主区长度必须介于 1 和 45 之间")
    public String getPairOne() {
        return pairOne;
    }

    public void setPairOne(String pairOne) {
        this.pairOne = pairOne;
    }

    @Length(min = 1, max = 45, message = "被交易区币种长度必须介于 1 和 45 之间")
    public String getPairOther() {
        return pairOther;
    }

    public void setPairOther(String pairOther) {
        this.pairOther = pairOther;
    }

    @Length(min = 1, max = 64, message = "用户id长度必须介于 1 和 64 之间")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Length(min = 0, max = 32, message = "手续费长度必须介于 0 和 32 之间")
    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

}