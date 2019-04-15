package com.biao.reactive.data.mongo.service;

import com.biao.reactive.data.mongo.domain.kline.KlineStatLog;

import java.util.List;

/**
 * The interface Kline stat log service.
 *
 *  ""(Myth)
 */
public interface KlineStatLogService {

    /**
     * Insert.
     *
     * @param klineStatLog the kline stat log
     */
    void insert(KlineStatLog klineStatLog);

    void batchInsert(List<KlineStatLog> klineStatLog);
}
