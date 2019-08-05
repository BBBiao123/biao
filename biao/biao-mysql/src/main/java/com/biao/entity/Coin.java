package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 用户资产
 */
@SqlTable("js_plat_coin")
public class Coin {

    private static final long serialVersionUID = 1L;
    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("name")
    private String name;

    @SqlField("full_name")
    private String fullName;
    /**
     * 官网
     */
    @SqlField(value = "domain")
    private String domain;
    /**
     * 白皮书地址
     */
    @SqlField(value = "whitepaper_url")
    private String whitepaperUrl;
    /**
     * token 总量
     */
    @SqlField(value = "token_volume")
    private String tokenVolume;
    /**
     * ico 价格
     */
    @SqlField(value = "ico_price")
    private String icoPrice;
    /**
     * 流通总量
     */
    @SqlField(value = "circulate_volume")
    private String circulateVolume;

    /**
     * 充值提现状态
     */
    @SqlField(value = "token_status")
    private String tokenStatus;
    /**
     * 状态 0：下架 1：上架
     */
    @SqlField(value = "status")
    private String status;
    /**
     * 最低挂单数量
     */
    @SqlField(value = "ex_min_volume")
    private BigDecimal exMinVolume;
    /**
     * 一次提现最低数量
     */
    @SqlField(value = "withdraw_min_volume")
    private BigDecimal withdrawMinVolume;
    /**
     * 一次提现最大数量
     */
    @SqlField(value = "withdraw_max_volume")
    private BigDecimal withdrawMaxVolume;
    /**
     * 一天最大提现额度
     */
    @SqlField(value = "withdraw_day_max_volume")
    private BigDecimal withdrawDayMaxVolume;

    /**
     * V1一天最大提现额度
     */
    @SqlField(value = "withdraw_day_one_max_volume")
    private BigDecimal withdrawDayOneMaxVolume;

    /**
     * V2一天最大提现额度
     */
    @SqlField(value = "withdraw_day_two_max_volume")
    private BigDecimal withdrawDayTwoMaxVolume;
    /**
     * 提现手续费
     */
    @SqlField(value = "withdraw_fee")
    private BigDecimal withdrawFee;
    /**
     * 提现手续费类型 0比例 1固定
     */
    @SqlField(value = "withdraw_fee_type")
    private String withdrawFeeType;
    /**
     * 1:基于以太  2:基于量子 3：基于小蚂 4：基于EOS5:cny
     **/
    @SqlField(value = "coin_type")
    private String coinType;
    @SqlField(value = "parent_id")
    private String parentId;
    /**
     * 币种图标
     */
    @SqlField(value = "icon_id")
    private String iconId;
    /**
     * 币种介绍
     */
    @SqlField(value = "remarks")
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getWhitepaperUrl() {
        return whitepaperUrl;
    }

    public void setWhitepaperUrl(String whitepaperUrl) {
        this.whitepaperUrl = whitepaperUrl;
    }

    public String getTokenVolume() {
        return tokenVolume;
    }

    public void setTokenVolume(String tokenVolume) {
        this.tokenVolume = tokenVolume;
    }

    public String getIcoPrice() {
        return icoPrice;
    }

    public void setIcoPrice(String icoPrice) {
        this.icoPrice = icoPrice;
    }

    public String getCirculateVolume() {
        return circulateVolume;
    }

    public void setCirculateVolume(String circulateVolume) {
        this.circulateVolume = circulateVolume;
    }

    public String getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(String tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getExMinVolume() {
        return exMinVolume;
    }

    public void setExMinVolume(BigDecimal exMinVolume) {
        this.exMinVolume = exMinVolume;
    }

    public BigDecimal getWithdrawMinVolume() {
        return withdrawMinVolume;
    }

    public void setWithdrawMinVolume(BigDecimal withdrawMinVolume) {
        this.withdrawMinVolume = withdrawMinVolume;
    }

    public BigDecimal getWithdrawMaxVolume() {
        return withdrawMaxVolume;
    }

    public void setWithdrawMaxVolume(BigDecimal withdrawMaxVolume) {
        this.withdrawMaxVolume = withdrawMaxVolume;
    }

    public BigDecimal getWithdrawDayMaxVolume() {
        return withdrawDayMaxVolume;
    }

    public void setWithdrawDayMaxVolume(BigDecimal withdrawDayMaxVolume) {
        this.withdrawDayMaxVolume = withdrawDayMaxVolume;
    }

    public BigDecimal getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(BigDecimal withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public String getWithdrawFeeType() {
        return withdrawFeeType;
    }

    public void setWithdrawFeeType(String withdrawFeeType) {
        this.withdrawFeeType = withdrawFeeType;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getWithdrawDayOneMaxVolume() {
        return withdrawDayOneMaxVolume;
    }

    public void setWithdrawDayOneMaxVolume(BigDecimal withdrawDayOneMaxVolume) {
        this.withdrawDayOneMaxVolume = withdrawDayOneMaxVolume;
    }

    public BigDecimal getWithdrawDayTwoMaxVolume() {
        return withdrawDayTwoMaxVolume;
    }

    public void setWithdrawDayTwoMaxVolume(BigDecimal withdrawDayTwoMaxVolume) {
        this.withdrawDayTwoMaxVolume = withdrawDayTwoMaxVolume;
    }
}
