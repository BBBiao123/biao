package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

@SqlTable("js_plat_user_oplog")
public class PlatUserOplog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @SqlField("user_id")
    private String userId;
    @SqlField("type")
    private String type;
    @SqlField("mobile")
    private String mobile;
    @SqlField("mail")
    private String mail;
    @SqlField("real_name")
    private String realName;
    @SqlField("content")
    private String content;
    @SqlField("reason")
    private String reason;
    @SqlField("create_by_name")
    private String createByName;
    @SqlField("update_by_name")
    private String updateByName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }
}
