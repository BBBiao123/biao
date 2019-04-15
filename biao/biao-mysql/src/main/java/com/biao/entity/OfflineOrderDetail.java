package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户c2c挂单表
 */
@SqlTable("js_plat_offline_order_detail")
public class OfflineOrderDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("user_name")
    private String userName;
    @SqlField("user_mobile")
    private String userMobile;
    @SqlField("ask_user_id")
    private String askUserId;
    @SqlField("ask_user_name")
    private String askUserName;
    @SqlField("ask_user_mobile")
    private String askUserMobile;
    @SqlField("alipay_no")
    private String alipayNo;
    @SqlField("alipay_qrcode_id")
    private String alipayQrcodeId;
    @SqlField("wechat_no")
    private String wechatNo;
    @SqlField("wechat_qrcode_id")
    private String wechatQrcodeId;
    @SqlField("sell_bank_no")
    private String sellBankNo;
    @SqlField("sell_bank_name")
    private String sellBankName;
    @SqlField("sell_bank_branch_name")
    private String sellBankBranchName;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("fee_volume")
    private BigDecimal feeVolume;
    @SqlField("price")
    private BigDecimal price;
    @SqlField("order_id")
    private String orderId;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("symbol")
    private String symbol;
    @SqlField("status")
    private Integer status;
    @SqlField("radom_num")
    private String radomNum;
    @SqlField("total_price")
    private BigDecimal totalPrice;
    @SqlField("remarks")
    private String remarks;
    @SqlField("ex_type")
    private Integer exType;
    @SqlField("advert_type")
    private Integer advertType;
    @SqlField("sub_order_id")
    private String subOrderId;
    @SqlField("sync_date")
    private LocalDateTime syncDate;
    @SqlField("confirm_receipt_date")
    private LocalDateTime confirmReceiptDate;
    @SqlField("confirm_payment_date")
    private LocalDateTime confirmPaymentDate;
    @SqlField("cancel_date")
    private LocalDateTime cancelDate;
    private int remainingTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getAskUserId() {
        return askUserId;
    }

    public void setAskUserId(String askUserId) {
        this.askUserId = askUserId;
    }

    public String getAskUserName() {
        return askUserName;
    }

    public void setAskUserName(String askUserName) {
        this.askUserName = askUserName;
    }

    public String getAskUserMobile() {
        return askUserMobile;
    }

    public void setAskUserMobile(String askUserMobile) {
        this.askUserMobile = askUserMobile;
    }

    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    public String getAlipayQrcodeId() {
        return alipayQrcodeId;
    }

    public void setAlipayQrcodeId(String alipayQrcodeId) {
        this.alipayQrcodeId = alipayQrcodeId;
    }

    public String getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(String wechatNo) {
        this.wechatNo = wechatNo;
    }

    public String getWechatQrcodeId() {
        return wechatQrcodeId;
    }

    public void setWechatQrcodeId(String wechatQrcodeId) {
        this.wechatQrcodeId = wechatQrcodeId;
    }

    public String getSellBankNo() {
        return sellBankNo;
    }

    public void setSellBankNo(String sellBankNo) {
        this.sellBankNo = sellBankNo;
    }

    public String getSellBankName() {
        return sellBankName;
    }

    public void setSellBankName(String sellBankName) {
        this.sellBankName = sellBankName;
    }

    public String getSellBankBranchName() {
        return sellBankBranchName;
    }

    public void setSellBankBranchName(String sellBankBranchName) {
        this.sellBankBranchName = sellBankBranchName;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getFeeVolume() {
        return feeVolume;
    }

    public void setFeeVolume(BigDecimal feeVolume) {
        this.feeVolume = feeVolume;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRadomNum() {
        return radomNum;
    }

    public void setRadomNum(String radomNum) {
        this.radomNum = radomNum;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getExType() {
        return exType;
    }

    public void setExType(Integer exType) {
        this.exType = exType;
    }

    public Integer getAdvertType() {
        return advertType;
    }

    public void setAdvertType(Integer advertType) {
        this.advertType = advertType;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public LocalDateTime getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(LocalDateTime syncDate) {
        this.syncDate = syncDate;
    }

    public LocalDateTime getConfirmReceiptDate() {
        return confirmReceiptDate;
    }

    public void setConfirmReceiptDate(LocalDateTime confirmReceiptDate) {
        this.confirmReceiptDate = confirmReceiptDate;
    }

    public LocalDateTime getConfirmPaymentDate() {
        return confirmPaymentDate;
    }

    public void setConfirmPaymentDate(LocalDateTime confirmPaymentDate) {
        this.confirmPaymentDate = confirmPaymentDate;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
}
