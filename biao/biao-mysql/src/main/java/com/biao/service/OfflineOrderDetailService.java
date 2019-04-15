package com.biao.service;

import com.biao.entity.OfflineOrderDetail;
import com.biao.entity.UserBank;
import com.biao.pojo.ResponsePage;
import com.biao.vo.OfflineConfirmVO;
import com.biao.vo.OrderDetailVO;

import java.util.List;

public interface OfflineOrderDetailService {

    OfflineOrderDetail findById(String id);

    OfflineOrderDetail findByUserIdAndOrderId(String userId, String orderId);

    String buy(OfflineOrderDetail order);

    void updateById(OfflineOrderDetail order);

    String sell(OfflineOrderDetail offlineOrderDetail, String userId, String mobile, String tag, String alipayNo, String alipayQrcodeId, String wechatNo, String wechatQrcodeId, UserBank userBank);

    void updateStatusBySubOrderId(OfflineConfirmVO confirmVO);

    List<OfflineOrderDetail> myOrderDetail(String userId);

    ResponsePage<OfflineOrderDetail> myOrderDetail(OrderDetailVO orderDetailVO);

    void updateStatusBySubOrderIdAndUnLock(OfflineConfirmVO offlineConfirmVO, String sellTag);

    void detailCancel(OfflineConfirmVO offlineConfirmVO,String tag);

    void detailShensu(OfflineConfirmVO offlineConfirmVO);

    List<OfflineOrderDetail> findByOrderId(String userId, String orderId);
}
