package com.biao.entity;

import com.biao.config.CustomLocalDateTimeDeserializer;
import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户c2c挂单表
 */
@SqlTable("js_plat_offline_order")
public class OfflineOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("create_by")
    protected String createBy;

    @SqlField("update_by")
    protected String updateBy;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    protected LocalDateTime createDate;

    @SqlField("update_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime updateDate;

    @SqlField("user_id")
    private String userId;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("lock_volume")
    private BigDecimal lockVolume;
    @SqlField("success_volume")
    private BigDecimal successVolume;
    @SqlField("fee_volume")
    private BigDecimal feeVolume;
    @SqlField("min_ex_volume")
    private BigDecimal minExVolume;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("symbol")
    private String symbol;
    @SqlField("ex_type")
    private Integer exType;
    @SqlField("status")
    private Integer status;
    @SqlField("flag")
    private Integer flag;
    @SqlField("price")
    private BigDecimal price;
    @SqlField("total_price")
    private BigDecimal totalPrice;
    @SqlField("remarks")
    private String remarks;
    @SqlField("real_name")
    private String realName;
    @SqlField("mobile")
    private String mobile;
    @SqlField("card_no")
    private String cardNo;
    @SqlField("bank_name")
    private String bankName;
    @SqlField("bank_branch_name")
    private String bankBranchName;
    @SqlField("wechat_no")
    private String wechatNo;
    @SqlField("wechat_qrcode_id")
    private String wechatQrcodeId;
    @SqlField("alipay_no")
    private String alipayNo;
    @SqlField("alipay_qrcode_id")
    private String alipayQrcodeId;
    @SqlField("version")
    private Long version;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getLockVolume() {
        return lockVolume;
    }

    public void setLockVolume(BigDecimal lockVolume) {
        this.lockVolume = lockVolume;
    }

    public BigDecimal getSuccessVolume() {
        return successVolume;
    }

    public void setSuccessVolume(BigDecimal successVolume) {
        this.successVolume = successVolume;
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

    public Integer getExType() {
        return exType;
    }

    public void setExType(Integer exType) {
        this.exType = exType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(String wechatNo) {
        this.wechatNo = wechatNo;
    }

    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardNo() {
        return cardNo;
    }

    public BigDecimal getFeeVolume() {
        return feeVolume;
    }

    public void setFeeVolume(BigDecimal feeVolume) {
        this.feeVolume = feeVolume;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getMinExVolume() {
        return minExVolume;
    }

    public void setMinExVolume(BigDecimal minExVolume) {
        this.minExVolume = minExVolume;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getWechatQrcodeId() {
        return wechatQrcodeId;
    }

    public void setWechatQrcodeId(String wechatQrcodeId) {
        this.wechatQrcodeId = wechatQrcodeId;
    }

    public String getAlipayQrcodeId() {
        return alipayQrcodeId;
    }

    public void setAlipayQrcodeId(String alipayQrcodeId) {
        this.alipayQrcodeId = alipayQrcodeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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
}
