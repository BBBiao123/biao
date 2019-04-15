package com.biao.service;

import com.biao.entity.PhoneGeocoder;

/**
 * 用户电话归属地服务接口
 *
 *  ""dministrator
 */
public interface UserPhoneGeocoderService {

    void insert(PhoneGeocoder phoneGeocoder);

    /**
     * 任务补全之前注册的用户
     */
    void onceTaskCompletionInsert();
}
