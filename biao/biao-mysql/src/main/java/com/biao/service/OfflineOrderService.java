package com.biao.service;

import com.biao.entity.OfflineOrder;
import com.biao.pojo.ResponsePage;
import com.biao.vo.AdvertVO;
import com.biao.vo.OfflineConfirmVO;
import com.biao.vo.OfflineOrderVO;

import java.util.List;

public interface OfflineOrderService {

    OfflineOrder findById(String id);

    String save(OfflineOrder order, String tag);

    void updateById(OfflineOrder order);

    List<OfflineOrder> findMyAdvertList(String userId);

    ResponsePage<OfflineOrder> findMyAdvertList(OfflineOrderVO offlineOrderVO);

    ResponsePage<OfflineOrder> findPage(AdvertVO advertVO);

    void advertCancel(OfflineConfirmVO offlineConfirmVO);

    void findByStatusAndExType();
}
