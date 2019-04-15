package com.biao.reactive.data.mongo.service;

import com.biao.pojo.ResponsePage;
import com.biao.query.UserLoginLogQuery;
import com.biao.reactive.data.mongo.domain.UserLoginLog;

public interface UserLoginLogService {

    void insert(UserLoginLog userLoginLog);

    /**
     * 分页查询登录日志
     *
     * @param userLoginLogQuery
     * @return
     */
    ResponsePage<UserLoginLog> findPage(UserLoginLogQuery userLoginLogQuery);
}
