package com.biao.vo;

import com.biao.pojo.RequestQuery;

public class UserStatisticsInfoVO extends RequestQuery {
    private String userId;
    private String coinSymbol;
    private String releaseType;// 释放类型，1普通会员2节点人3合伙人

    private String relationId; // 业务关联ID，releaseType=1时，relationId=用户冻结表主键ID

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
}
