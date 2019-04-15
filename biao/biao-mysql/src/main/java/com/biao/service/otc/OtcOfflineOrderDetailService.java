package com.biao.service.otc;

import com.biao.entity.otc.OtcOfflineOrderDetail;
import com.biao.entity.otc.OtcOfflineOrderDetailPay;
import com.biao.pojo.ResponsePage;
import com.biao.vo.otc.OtcOfflineDetailCountVO;
import com.biao.vo.otc.OtcOfflineOrderDetailVO;

import java.util.List;

public interface OtcOfflineOrderDetailService {

    List<OtcOfflineOrderDetailPay> findPayByOfflineSubOrder(String userId, String subOrderId);

    OtcOfflineOrderDetail findByUserIdAndDetailId(String userId, String subOrderId, String loginSource);

    ResponsePage<OtcOfflineOrderDetail> findMySuborderByStatus(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO);

    ResponsePage<OtcOfflineOrderDetail> findAllSubOrder(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO);

    OtcOfflineDetailCountVO countOrderDetail(String userId);

    void buy(OtcOfflineOrderDetail offlineOrderDetail);

    void confirmPayment(OtcOfflineOrderDetailVO offlineConfirmVO);

    void sell(OtcOfflineOrderDetail offlineOrderDetail, String detailPay);

    void confirmReceipt(OtcOfflineOrderDetailVO offlineConfirmVO);

    void doConfirmReceiptNoAppeal(OtcOfflineOrderDetailVO offlineConfirmVO, String batchNo);

    void cancelOrderDetail(OtcOfflineOrderDetailVO offlineConfirmVO);

    void doCancelOrderDetailNoAppeal(OtcOfflineOrderDetailVO offlineConfirmVO, String batchNo);


}
