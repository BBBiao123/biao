package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.time.LocalDateTime;

/**
 * 用户coin 提现地址
 */
@SqlTable("js_plat_user_withdraw_address")
public class WithdrawAddress {
    private static final long serialVersionUID = 1L;
    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;
    @SqlField("user_id")
    private String userId;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("address")
    private String address;
    @SqlField("status")
    private String status;
    @SqlField("type")
    private String type;
    @SqlField("tag")
    private String tag;
    @SqlField("create_date")
    protected LocalDateTime createDate;
    @SqlField("update_date")
    protected LocalDateTime updateDate;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
