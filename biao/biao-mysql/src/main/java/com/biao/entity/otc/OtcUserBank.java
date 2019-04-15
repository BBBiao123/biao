package com.biao.entity.otc;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

/**
 * otc用户银行卡添加
 */
@SqlTable("otc_user_bank")
public class OtcUserBank extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @SqlField("type")
    private String type;
    @SqlField("pay_no")
    private String payNo;
    @SqlField("qrcode_id")
    private String qrcodeId;
    @SqlField("user_id")
    private String userId;
    @SqlField("bank_name")
    private String bankName;
    @SqlField("branch_bank_name")
    private String branchBankName;
    @SqlField("country")
    private String country;
    @SqlField("status")
    private String status;
    @SqlField("mobile")
    private String mobile;
    @SqlField("real_name")
    private String realName;
    @SqlField("card_no")
    private String cardNo;
    @SqlField("support_currency_code")
    private String supportCurrencyCode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchBankName() {
        return branchBankName;
    }

    public void setBranchBankName(String branchBankName) {
        this.branchBankName = branchBankName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getSupportCurrencyCode() {
        return supportCurrencyCode;
    }

    public void setSupportCurrencyCode(String supportCurrencyCode) {
        this.supportCurrencyCode = supportCurrencyCode;
    }
}
