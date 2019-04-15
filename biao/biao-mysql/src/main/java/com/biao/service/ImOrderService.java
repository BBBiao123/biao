package com.biao.service;

import com.biao.kafka.interceptor.ImParamDTO;

/**
 * The interface Im order service.
 *
 *  ""(Myth)
 */
public interface ImOrderService {


    /**
     * Send offline order.
     *
     * @param imParamDTO the im param dto
     */
    void sendOfflineOrder(ImParamDTO imParamDTO);
}
