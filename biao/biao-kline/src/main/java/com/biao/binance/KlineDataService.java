package com.biao.binance;

import com.biao.binance.config.KlinePullConfig;

import java.text.ParseException;

/**
 * The interface Kline data service.
 *
 *  ""(Myth)
 */
public interface KlineDataService {

    /**
     * Handler pull data.
     *
     * @param config the config
     * @throws ParseException the parse exception
     */
    void handlerPullData(KlinePullConfig config) throws ParseException;

}
