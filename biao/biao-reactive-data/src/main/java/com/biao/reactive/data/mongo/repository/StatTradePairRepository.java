package com.biao.reactive.data.mongo.repository;

import com.biao.reactive.data.mongo.domain.StatTradePair;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/21 下午7:32
 * @since JDK 1.8
 */
public interface StatTradePairRepository extends ReactiveMongoRepository<StatTradePair, String> {
}
