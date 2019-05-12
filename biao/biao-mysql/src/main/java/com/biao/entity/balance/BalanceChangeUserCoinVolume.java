package com.biao.entity.balance;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 用户余币宝资产
 *
 *  ""
 */
@SqlTable("js_plat_user_coin_balancechange")
public class BalanceChangeUserCoinVolume extends BaseEntity {

    @SqlField("user_id")
    private String userId;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("flag")
    private int flag;

    @SqlField("coin_num")
    private BigDecimal coinNum;

    @SqlField("mobile")
    private String mobile;

    @SqlField("mail")
    private String mail;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public BigDecimal getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(BigDecimal coinNum) {
        this.coinNum = coinNum;
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
}
