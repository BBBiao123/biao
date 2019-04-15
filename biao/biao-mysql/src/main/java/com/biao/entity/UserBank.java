package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

/**
 * 用户银行卡添加
 */
@SqlTable("js_plat_user_bank")
public class UserBank extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("bank_name")
    private String bankName;
    @SqlField("branch_bank_name")
    private String branchBankName;
    @SqlField("status")
    private Integer status;
    @SqlField("mobile")
    private String mobile;
    @SqlField("real_name")
    private String realName;
    @SqlField("card_no")
    private String cardNo;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
}
