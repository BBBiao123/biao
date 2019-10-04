package com.biao.entity.balance;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @SqlField("coin_plat_symbol")
    private String coinPlatSymbol;

    @SqlField("flag")
    private int flag;

    @SqlField("coin_num")
    private BigDecimal coinNum;

    @SqlField("mobile")
    private String mobile;

    @SqlField("mail")
    private String mail;

    @SqlField("accumul_income")
    private BigDecimal accumulIncome;

    @SqlField("take_out_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime takeOutDate;

    @SqlField("balance_id")
    private String balanceId;

    @SqlField("contract_time")
    private Integer contractTime;

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

    public BigDecimal getAccumulIncome() {
        return accumulIncome;
    }

    public void setAccumulIncome(BigDecimal accumulIncome) {
        this.accumulIncome = accumulIncome;
    }

    public LocalDateTime getTakeOutDate() {
        return takeOutDate;
    }

    public void setTakeOutDate(LocalDateTime takeOutDate) {
        this.takeOutDate = takeOutDate;
    }

    public String getCoinPlatSymbol() {
        return coinPlatSymbol;
    }

    public void setCoinPlatSymbol(String coinPlatSymbol) {
        this.coinPlatSymbol = coinPlatSymbol;
    }

    public String getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(String balanceId) {
        this.balanceId = balanceId;
    }

    public Integer getContractTime() {
        return contractTime;
    }

    public void setContractTime(Integer contractTime) {
        this.contractTime = contractTime;
    }
}
