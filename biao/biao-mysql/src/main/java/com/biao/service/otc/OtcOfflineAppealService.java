package com.biao.service.otc;

import com.biao.entity.otc.OtcOfflineAppeal;

import java.util.List;

public interface OtcOfflineAppealService {
    /**
     * 根据OTC订单ID查询关联的申诉单
     *
     * @param subOrderId
     * @return
     */
    List<OtcOfflineAppeal> findBySubOrderId(String subOrderId);

    /**
     * 保存申诉单
     *
     * @param offlineAppeal
     * @return
     */
    String save(OtcOfflineAppeal offlineAppeal);

    /**
     * 根据用户ID查询用户下所有的申诉单
     *
     * @param userId
     * @return
     */
    List<OtcOfflineAppeal> findAllAppeal(String userId, String loginSource);

    /**
     * 根据用户ID和申诉单ID撤销申诉单
     *
     * @param userId
     * @param appealId
     */
    void cancelAppeal(String userId, String appealId);
}
