package com.biao.reactive.data.mongo.service;

import com.biao.reactive.data.mongo.domain.kline.KlineLog;

import java.time.LocalDateTime;
import java.util.List;

public interface KlineLogService {

    public void insert(KlineLog klineLog);

    public void batchInsert(List<KlineLog> klineLog);

    List<KlineLog> findStatisticsTradeByCoinMain(String coinMain, String coinOther, Class<?> collectionName, LocalDateTime startDate, LocalDateTime lastDate);

    public void delete(String coinMain, String coinOther, LocalDateTime startDate, LocalDateTime lastDate);
}
