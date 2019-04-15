package com.biao.entity;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("mk_auto_trade_monitor")
public class MkAutoTradeMonitor extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("setting_id")
    private String settingId;
    @SqlField("type")
    private String type;
    @SqlField("status")
    private String status;
    @SqlField("user_id")
    private String userId;
    @SqlField("username")
    private String username;
    @SqlField("mail")
    private String mail;
    @SqlField("mobile")
    private String mobile;
    @SqlField("coin_main_symbol")
    private String coinMainSymbol;
    @SqlField("coin_other_symbol")
    private String coinOtherSymbol;
    @SqlField("begin_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime beginDate;
    @SqlField("end_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endDate;
    @SqlField("min_volume")
    private BigDecimal minVolume;
    @SqlField("max_volume")
    private BigDecimal maxVolume;
    @SqlField("min_price")
    private BigDecimal minPrice;
    @SqlField("max_price")
    private BigDecimal maxPrice;
    @SqlField("frequency")
    private Integer frequency;
    @SqlField("time_unit")
    private String timeUnit;
    @SqlField("order_number")
    private Integer orderNumber;
    @SqlField("order_volume")
    private BigDecimal orderVolume;
    @SqlField("order_price")
    private BigDecimal orderPrice;
    @SqlField("remark")
    private String remark;
    @SqlField("create_by_name")
    private String createByName;

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
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

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }
}
