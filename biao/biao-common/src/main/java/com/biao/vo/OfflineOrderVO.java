package com.biao.vo;

import com.biao.pojo.RequestQuery;

public class OfflineOrderVO extends RequestQuery {

    private static final long serialVersionUID = 1L;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
