package com.biao.service.push;

import com.biao.entity.OfflineOrderDetail;

/**
 * The interface Offline order push service.
 *
 *  ""(Myth)
 */
public interface OfflineOrderPushService {

    /**
     * Push data.
     *
     * @param userId     the user id
     * @param subOrderId the sub order id
     * @param status     the status
     */
    void pushData(String userId, String subOrderId, Integer status);


    /**
     * Push websocket offline order detail.
     *
     * @param userId     the user id
     * @param subOrderId the sub order id
     * @param status     the status
     * @return the offline order detail
     */
    OfflineOrderDetail pushWebsocket(String userId, String subOrderId, Integer status);
}
