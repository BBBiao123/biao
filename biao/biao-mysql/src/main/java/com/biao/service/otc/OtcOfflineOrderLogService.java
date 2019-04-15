package com.biao.service.otc;

import com.biao.entity.otc.OtcOfflineOrder;

public interface OtcOfflineOrderLogService {

    void saveLog(OtcOfflineOrder otcOfflineOrder, String batchNo);

    void saveLog(String orderId, String batchNo);

}
