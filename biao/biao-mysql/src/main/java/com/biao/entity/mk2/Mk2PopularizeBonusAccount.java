package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

/**
 * 平台运营分红账户
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_bonus_account")
public class Mk2PopularizeBonusAccount {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("type")
    private String type;        //

    @SqlField("user_id")
    private String userId;        //

    @SqlField("mail")
    private String mail;        //

    @SqlField("mobile")
    private String mobile;        //

    @SqlField("id_card")
    private String idCard;        //

    @SqlField("real_name")
    private String realName;        //

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;        //

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;        //

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
