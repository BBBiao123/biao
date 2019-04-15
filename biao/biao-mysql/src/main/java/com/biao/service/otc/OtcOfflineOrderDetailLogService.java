package com.biao.service.otc;

import com.biao.entity.otc.OtcOfflineOrderDetail;

public interface OtcOfflineOrderDetailLogService {

    void saveLog(OtcOfflineOrderDetail otcOfflineOrderDetail, String batchNo);

    void saveLog(String userId, String subOrderId, String loginSource, String batchNo);

    void saveLog(String subOrderId, String batchNo);
}
