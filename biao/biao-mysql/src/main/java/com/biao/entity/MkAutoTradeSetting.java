package com.biao.entity;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("mk_auto_trade_setting")
public class MkAutoTradeSetting extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("type")
    private String type;
    @SqlField("status")
    private String status;
    @SqlField("user_id")
    private String userId;
    @SqlField("username")
    private String username;
    @SqlField("pass")
    private String pass;
    @SqlField("mail")
    private String mail;
    @SqlField("mobile")
    private String mobile;
    @SqlField("coin_main_id")
    private String coinMainId;
    @SqlField("coin_main_symbol")
    private String coinMainSymbol;
    @SqlField("coin_other_id")
    private String coinOtherId;
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
    @SqlField("price_precision")
    private Integer pricePrecision;
    @SqlField("volume_precision")
    private Integer volumePrecision;
    @SqlField("time_unit")
    private String timeUnit;
    @SqlField("remark")
    private String remark;
    @SqlField("create_by_name")
    private String createByName;

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

    public String getCoinMainId() {
        return coinMainId;
    }

    public void setCoinMainId(String coinMainId) {
        this.coinMainId = coinMainId;
    }

    public String getCoinMainSymbol() {
        return coinMainSymbol;
    }

    public void setCoinMainSymbol(String coinMainSymbol) {
        this.coinMainSymbol = coinMainSymbol;
    }

    public String getCoinOtherId() {
        return coinOtherId;
    }

    public void setCoinOtherId(String coinOtherId) {
        this.coinOtherId = coinOtherId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }
}
