package com.biao.service.otc;

import com.biao.entity.otc.OtcOfflineOrder;
import com.biao.entity.otc.OtcOfflineOrderDetail;
import com.biao.pojo.ResponsePage;
import com.biao.vo.otc.OtcOfflineOrderVO;

public interface OtcOfflineOrderService {

    ResponsePage<OtcOfflineOrder> findMyAdvertList(OtcOfflineOrderVO otcOfflineOrderVO);

    ResponsePage<OtcOfflineOrder> findPage(OtcOfflineOrderVO otcOfflineOrderVO);

    void advertCancel(OtcOfflineOrderVO otcOfflineOrderVO);

    OtcOfflineOrder save(OtcOfflineOrder offlineOrder, String supportBank);

    void updateOtcOrderVolume(OtcOfflineOrder otcOfflineOrder, String batchNo);

    void otcDetailSaveUpdateOrder(OtcOfflineOrderDetail offlineOrderDetail, OtcOfflineOrder otcOfflineOrder, String batchNo);
}
