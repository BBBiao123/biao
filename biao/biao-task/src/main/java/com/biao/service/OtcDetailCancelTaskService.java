package com.biao.service;

import com.biao.entity.otc.OtcOfflineOrderDetail;

public interface OtcDetailCancelTaskService {

    void doCancelOrderDetail();

    void cancelOrderDetail(OtcOfflineOrderDetail otcOfflineOrderDetail);
}
