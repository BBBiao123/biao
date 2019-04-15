package com.biao.entity.otc;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

/**
 * OTC广告支付方式
 */
@SqlTable("otc_offline_order_pay")
public class OtcOfflineOrderPay extends BaseEntity {

    @SqlField("order_id")
    private String orderId;
    @SqlField("type")
    private String type;
    @SqlField("support_currency_code")
    private String supportCurrencyCode;
    @SqlField("user_id")
    private String userId;
    @SqlField("real_name")
    private String realName;
    @SqlField("mobile")
    private String mobile;
    @SqlField("pay_no")
    private String payNo;
    @SqlField("qrcode_id")
    private String qrcodeId;
    @SqlField("bank_name")
    private String bankName;
    @SqlField("branch_bank_name")
    private String branchBankName;
    @SqlField("remarks")
    private String remarks;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSupportCurrencyCode() {
        return supportCurrencyCode;
    }

    public void setSupportCurrencyCode(String supportCurrencyCode) {
        this.supportCurrencyCode = supportCurrencyCode;
    }

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId;
    }
}
