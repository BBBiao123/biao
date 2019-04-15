package com.biao.reactive.data.mongo.service.impl;

import com.biao.reactive.data.mongo.domain.kline.KlineStatLog;
import com.biao.reactive.data.mongo.service.KlineStatLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  ""dministrator
 */
@Service("klineStatLogService")
public class KlineStatLogServiceImpl implements KlineStatLogService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public KlineStatLogServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void insert(KlineStatLog klineLog) {
        mongoTemplate.insert(klineLog);
    }

    @Override
    public void batchInsert(List<KlineStatLog> klineStatLog) {
        mongoTemplate.insertAll(klineStatLog);
    }
}
