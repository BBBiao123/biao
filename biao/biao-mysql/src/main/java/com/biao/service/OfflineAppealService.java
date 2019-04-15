package com.biao.service;

import com.biao.entity.OfflineAppeal;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;

import java.util.List;

public interface OfflineAppealService {

    /**
     * 根据c2c订单ID查询关联的申诉单
     *
     * @param subOrderId
     * @return
     */
    List<OfflineAppeal> findBySubOrderId(String subOrderId);

    /**
     * 保存申诉单
     *
     * @param offlineAppeal
     * @return
     */
    String save(OfflineAppeal offlineAppeal);

    /**
     * 根据用户ID查询用户下所有的申诉单
     *
     * @return
     */
    ResponsePage<OfflineAppeal> findAllAppeal(RequestQuery requestQuery, String userId);

    /**
     * 根据用户ID和申诉单ID撤销申诉单
     *
     * @param userId
     * @param appealId
     */
    void cancelAppeal(String userId, String appealId);

}
