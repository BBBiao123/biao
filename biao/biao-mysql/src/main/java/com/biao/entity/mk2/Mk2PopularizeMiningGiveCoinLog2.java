package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 挖矿规则送币流水
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_mining_give_coin_log2")
public class Mk2PopularizeMiningGiveCoinLog2 {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("type")
    private String type;

    @SqlField("user_id")
    private String userId;

    @SqlField("mail")
    private String mail;

    @SqlField("mobile")
    private String mobile;

    @SqlField("id_card")
    private String idCard;

    @SqlField("real_name")
    private String realName;

    @SqlField("coin_id")
    private String coinId;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("volume")
    private BigDecimal volume;

    @SqlField("total_volume")
    private BigDecimal totalVolume;

    @SqlField("ratio")
    private BigDecimal ratio;

    @SqlField("order_no")
    private Long orderNo;

    @SqlField("join_volume")
    private BigDecimal joinVolume;

    @SqlField("max_sub_volume")
    private BigDecimal maxSubVolume;

    @SqlField("count_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime countDate;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    @SqlField("team_hold_total")
    private BigDecimal teamHoldTotal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public LocalDateTime getCountDate() {
        return countDate;
    }

    public void setCountDate(LocalDateTime countDate) {
        this.countDate = countDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getJoinVolume() {
        return joinVolume;
    }

    public void setJoinVolume(BigDecimal joinVolume) {
        this.joinVolume = joinVolume;
    }

    public BigDecimal getMaxSubVolume() {
        return maxSubVolume;
    }

    public void setMaxSubVolume(BigDecimal maxSubVolume) {
        this.maxSubVolume = maxSubVolume;
    }

    public BigDecimal getTeamHoldTotal() {
        return teamHoldTotal;
    }

    public void setTeamHoldTotal(BigDecimal teamHoldTotal) {
        this.teamHoldTotal = teamHoldTotal;
    }
}
