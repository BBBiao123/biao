package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 自动交易挂单表
 */
@SqlTable("mk_auto_trade_order")
public class MkAutoTradeOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("setting_id")
    private String settingId;
    @SqlField("monitor_id")
    private String monitorId;
    @SqlField("user_id")
    private String userId;
    @SqlField("mail")
    private String mail;
    @SqlField("mobile")
    private String mobile;
    @SqlField("type")
    private String type;
    @SqlField("status")
    private String status;
    @SqlField("coin_main_symbol")
    private String coinMainSymbol;
    @SqlField("coin_other_symbol")
    private String coinOtherSymbol;
    @SqlField("price")
    private BigDecimal price;
    @SqlField("volume")
    private BigDecimal volume;
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

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
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
