package com.biao.service;

import com.biao.entity.kline.KlinePullConfig;

import java.text.ParseException;

/**
 * The interface Kline data service.
 *
 *  ""(Myth)
 */
public interface KlineDataService {

    /**
     * Execute pull data.
     */
    void executePullData();


    /**
     * 拉取币安交易所1小时以上的数据，只拉取一次
     */
    void executePullGroupData();

    /**
     * Handler pull data.
     *
     * @param config the config
     */
    void handlerPullData(KlinePullConfig config) throws ParseException;

}
