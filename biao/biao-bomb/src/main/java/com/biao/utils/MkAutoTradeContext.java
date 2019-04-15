package com.biao.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MkAutoTradeContext {

    private String settingId;
    private String monitorId;
    private String type;
    private String status;
    private String username;
    private String userId;
    private String mail;
    private String mobile;
    private String password;
    private String coinMainSymbol;
    private String coinOtherSymbol;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private BigDecimal minVolume;
    private BigDecimal maxVolume;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer pricePrecision;
    private Integer volumePrecision;
    private Integer frequency;
    private String timeUnit;
    private Integer orderNumber;
    private BigDecimal orderVolume;
    private BigDecimal orderPrice;
    private String remark;
    private String token;
    private String createBy;
    private String createByName;

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCoinMainSymbol() {
        return coinMainSymbol;
    }

    public void setCoinMainSymbol(String coinMainSymbol) {
        this.coinMainSymbol = coinMainSymbol;
    }

    public String getCoinOtherSymbol() {
        return coinOtherSymbol;
    }

    public void setCoinOtherSymbol(String coinOtherSymbol) {
        this.coinOtherSymbol = coinOtherSymbol;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(BigDecimal minVolume) {
        this.minVolume = minVolume;
    }

    public BigDecimal getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(BigDecimal maxVolume) {
        this.maxVolume = maxVolume;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getOrderVolume() {
        return orderVolume;
    }

    public void setOrderVolume(BigDecimal orderVolume) {
        this.orderVolume = orderVolume;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPricePrecision() {
        return pricePrecision;
    }

    public void setPricePrecision(Integer pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    public Integer getVolumePrecision() {
        return volumePrecision;
    }

    public void setVolumePrecision(Integer volumePrecision) {
        this.volumePrecision = volumePrecision;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }
}
