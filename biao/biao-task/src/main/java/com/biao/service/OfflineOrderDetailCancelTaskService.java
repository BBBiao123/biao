package com.biao.service;

import com.biao.entity.OfflineOrderDetail;

public interface OfflineOrderDetailCancelTaskService {

    void doCancelOrderDetail();

    void cancelOrderDetail(OfflineOrderDetail offlineOrderDetail);
}
