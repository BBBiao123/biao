package com.biao.entity.mk2;

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
 * 团队挖矿争霸排序
 *
 *  ""ongfeng
 * @version 2018-11-12
 */
@SqlTable("mk2_popularize_mining_team_sort")
public class Mk2PopularizeMiningTeamSort implements Serializable {

    private static final long serialVersionUID = 1L;
    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("user_id")
    private String userId;

    @SqlField("mail")
    private String mail;

    @SqlField("mobile")
    private String mobile;

    @SqlField("volume")
    private BigDecimal volume;

    private String volumeStr;

    @SqlField("order_no")
    private String orderNo;

    @SqlField("real_name")
    private String realName;

    @SqlField("sort_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime sortDate;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createDate;

    @SqlField("createBy")
    private String createBy;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public LocalDateTime getSortDate() {
        return sortDate;
    }

    public void setSortDate(LocalDateTime sortDate) {
        this.sortDate = sortDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getVolumeStr() {
        return volumeStr;
    }

    public void setVolumeStr(String volumeStr) {
        this.volumeStr = volumeStr;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
