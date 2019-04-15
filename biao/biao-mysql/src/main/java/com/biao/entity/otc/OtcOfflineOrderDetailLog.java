package com.biao.entity.otc;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * OTC订单变更流水
 */
@SqlTable("otc_offline_order_detail_log")
public class OtcOfflineOrderDetailLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("order_id")
    private String orderId;
    @SqlField("order_user_id")
    private String orderUserId;
    @SqlField("sub_order_id")
    private String subOrderId;
    @SqlField("support_currency_code")
    private String supportCurrencyCode;
    @SqlField("publish_source")
    private String publishSource;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("symbol")
    private String symbol;
    @SqlField("price")
    private BigDecimal price;
    @SqlField("total_price")
    private BigDecimal totalPrice;
    @SqlField("user_mobile")
    private String userMobile;
    @SqlField("real_name")
    private String realName;
    @SqlField("user_id")
    private String userId;
    @SqlField("ex_type")
    private String exType;
    @SqlField("ask_user_mobile")
    private String askUserMobile;
    @SqlField("ask_real_name")
    private String askRealName;
    @SqlField("ask_user_id")
    private String askUserId;
    @SqlField("remarks")
    private String remarks;
    @SqlField("status")
    private String status;
    @SqlField("radom_num")
    private String radomNum;
    @SqlField("confirm_receipt_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime confirmReceiptDate;
    @SqlField("confirm_payment_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime confirmPaymentDate;
    @SqlField("batch_no")
    private String batchNo;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime createDate;

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime updateDate;

    @SqlField("cancel_date")
    private LocalDateTime cancelDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExType() {
        return exType;
    }

    public void setExType(String exType) {
        this.exType = exType;
    }

    public String getAskUserMobile() {
        return askUserMobile;
    }

    public void setAskUserMobile(String askUserMobile) {
        this.askUserMobile = askUserMobile;
    }

    public String getAskUserId() {
        return askUserId;
    }

    public void setAskUserId(String askUserId) {
        this.askUserId = askUserId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRadomNum() {
        return radomNum;
    }

    public void setRadomNum(String radomNum) {
        this.radomNum = radomNum;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }


    public String getSupportCurrencyCode() {
        return supportCurrencyCode;
    }

    public void setSupportCurrencyCode(String supportCurrencyCode) {
        this.supportCurrencyCode = supportCurrencyCode;
    }

    public String getPublishSource() {
        return publishSource;
    }

    public void setPublishSource(String publishSource) {
        this.publishSource = publishSource;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAskRealName() {
        return askRealName;
    }

    public void setAskRealName(String askRealName) {
        this.askRealName = askRealName;
    }

    public String getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(String orderUserId) {
        this.orderUserId = orderUserId;
    }
}
