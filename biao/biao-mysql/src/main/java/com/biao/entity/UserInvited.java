package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

@SqlTable("js_plat_user_invited")
public class UserInvited extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("invited_user_id")
    private String invitedUserId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(String invitedUserId) {
        this.invitedUserId = invitedUserId;
    }
}
