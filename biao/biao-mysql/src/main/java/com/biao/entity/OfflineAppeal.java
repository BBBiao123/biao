package com.biao.entity;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户c2c申诉
 */
@SqlTable("js_plat_offline_appeal")
public class OfflineAppeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("appeal_user_id")
    private String appealUserId;//

    @SqlField("appeal_mail")
    private String appealMail;

    @SqlField("appeal_mobile")
    private String appealMobile;

    @SqlField("appeal_real_name")
    private String appealRealName;

    @SqlField("appeal_id_card")
    private String appealIdCard;

    @SqlField("sub_order_id")
    private String subOrderId;//

    @SqlField("examine_user_id")
    private String examineUserId;//

    @SqlField("status")
    private String status;//

    @SqlField("appeal_type")
    private String appealType;//

    @SqlField("reason")
    private String reason;//

    @SqlField("image_path")
    private String imagePath;//

    @SqlField("image_path2")
    private String imagePath2;//

    @SqlField("image_path3")
    private String imagePath3;//

    @SqlField("sell_user_id")
    private String sellUserId;//

    @SqlField("sell_mail")
    private String sellMail;

    @SqlField("sell_mobile")
    private String sellMobile;

    @SqlField("sell_real_name")
    private String sellRealName;

    @SqlField("sell_id_card")
    private String sellIdCard;

    @SqlField("buy_user_id")
    private String buyUserId;//

    @SqlField("buy_mail")
    private String buyMail;

    @SqlField("buy_mobile")
    private String buyMobile;

    @SqlField("buy_real_name")
    private String buyRealName;

    @SqlField("buy_id_card")
    private String buyIdCard;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime createDate;

    @SqlField("examine_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime examineDate;//

    @SqlField("sync_key")
    private String syncKey;

    public String getAppealUserId() {
        return appealUserId;
    }

    public void setAppealUserId(String appealUserId) {
        this.appealUserId = appealUserId;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getExamineUserId() {
        return examineUserId;
    }

    public void setExamineUserId(String examineUserId) {
        this.examineUserId = examineUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppealType() {
        return appealType;
    }

    public void setAppealType(String appealType) {
        this.appealType = appealType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public LocalDateTime getExamineDate() {
        return examineDate;
    }

    public void setExamineDate(LocalDateTime examineDate) {
        this.examineDate = examineDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getSellUserId() {
        return sellUserId;
    }

    public void setSellUserId(String sellUserId) {
        this.sellUserId = sellUserId;
    }

    public String getBuyUserId() {
        return buyUserId;
    }

    public void setBuyUserId(String buyUserId) {
        this.buyUserId = buyUserId;
    }

    public String getAppealMail() {
        return appealMail;
    }

    public void setAppealMail(String appealMail) {
        this.appealMail = appealMail;
    }

    public String getAppealMobile() {
        return appealMobile;
    }

    public void setAppealMobile(String appealMobile) {
        this.appealMobile = appealMobile;
    }

    public String getAppealRealName() {
        return appealRealName;
    }

    public void setAppealRealName(String appealRealName) {
        this.appealRealName = appealRealName;
    }

    public String getAppealIdCard() {
        return appealIdCard;
    }

    public void setAppealIdCard(String appealIdCard) {
        this.appealIdCard = appealIdCard;
    }

    public String getSellMail() {
        return sellMail;
    }

    public void setSellMail(String sellMail) {
        this.sellMail = sellMail;
    }

    public String getSellMobile() {
        return sellMobile;
    }

    public void setSellMobile(String sellMobile) {
        this.sellMobile = sellMobile;
    }

    public String getSellRealName() {
        return sellRealName;
    }

    public void setSellRealName(String sellRealName) {
        this.sellRealName = sellRealName;
    }

    public String getSellIdCard() {
        return sellIdCard;
    }

    public void setSellIdCard(String sellIdCard) {
        this.sellIdCard = sellIdCard;
    }

    public String getBuyMail() {
        return buyMail;
    }

    public void setBuyMail(String buyMail) {
        this.buyMail = buyMail;
    }

    public String getBuyMobile() {
        return buyMobile;
    }

    public void setBuyMobile(String buyMobile) {
        this.buyMobile = buyMobile;
    }

    public String getBuyRealName() {
        return buyRealName;
    }

    public void setBuyRealName(String buyRealName) {
        this.buyRealName = buyRealName;
    }

    public String getBuyIdCard() {
        return buyIdCard;
    }

    public void setBuyIdCard(String buyIdCard) {
        this.buyIdCard = buyIdCard;
    }

    public String getSyncKey() {
        return syncKey;
    }

    public void setSyncKey(String syncKey) {
        this.syncKey = syncKey;
    }
}
