package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 用户c2c转账记录
 */
@SqlTable("js_plat_offline_change_log")
public class OfflineChangeLog extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("fee")
    private BigDecimal fee;
    @SqlField("type")
    private String type;
    @SqlField("status")
    private String status;
    @SqlField("account")
    private String account;
    @SqlField("real_name")
    private String realName;
    @SqlField("other_user_id")
    private String otherUserId;
    @SqlField("other_account")
    private String otherAccount;
    @SqlField("other_real_name")
    private String otherRealName;
    @SqlField("change_no")
    private String changeNo;

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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherAccount() {
        return otherAccount;
    }

    public void setOtherAccount(String otherAccount) {
        this.otherAccount = otherAccount;
    }

    public String getOtherRealName() {
        return otherRealName;
    }

    public void setOtherRealName(String otherRealName) {
        this.otherRealName = otherRealName;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
