/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

/**
 * 注册用户送币Entity
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_register_coin")
public class Mk2PopularizeRegisterCoin {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("register_conf_id")
    private String registerConfId;        // 规则表ID

    @SqlField("conf_name")
    private String confName;        // 活动名称

    @SqlField("user_id")
    private String userId;        // 用户ID

    @SqlField("user_name")
    private String userName;        // 用户姓名

    @SqlField("mail")
    private String mail;    // 邮箱

    @SqlField("mobile")
    private String mobile; // 手机号

    @SqlField("volume")
    private Integer volume;        // 送币个数

    @SqlField("coin_id")
    private String coinId;        // 送币ID

    @SqlField("coin_symbol")
    private String coinSymbol;        // 送币名称

    @SqlField("for_user_id")
    private String forUserId;        // 注册用户ID

    @SqlField("status")
    private String status;        // 状态

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime beginCreateDate;        // 开始 创建时间

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endCreateDate;        // 结束 创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisterConfId() {
        return registerConfId;
    }

    public void setRegisterConfId(String registerConfId) {
        this.registerConfId = registerConfId;
    }

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
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

    public String getForUserId() {
        return forUserId;
    }

    public void setForUserId(String forUserId) {
        this.forUserId = forUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getBeginCreateDate() {
        return beginCreateDate;
    }

    public void setBeginCreateDate(LocalDateTime beginCreateDate) {
        this.beginCreateDate = beginCreateDate;
    }

    public LocalDateTime getEndCreateDate() {
        return endCreateDate;
    }

    public void setEndCreateDate(LocalDateTime endCreateDate) {
        this.endCreateDate = endCreateDate;
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