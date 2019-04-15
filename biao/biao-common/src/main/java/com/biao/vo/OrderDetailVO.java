package com.biao.vo;

import com.biao.pojo.RequestQuery;

public class OrderDetailVO extends RequestQuery {

    private static final long serialVersionUID = 1L;

    private String status;

    private String userId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
