package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 挖矿规则送币流水
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("js_plat_destroy_account_log")
public class DestroyAccountLog extends BaseEntity {

    @SqlField("user_id")
    private String userId;

    @SqlField("coin_id")
    private String coinId;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("mobile")
    private String mobile;

    @SqlField("mail")
    private String mail;

    @SqlField("volume")
    private BigDecimal volume;

    @SqlField("type")
    private String type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
