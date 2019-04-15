package com.biao.reactive.data.mongo.repository;


import com.biao.reactive.data.mongo.domain.MatchStream;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * 批量插入数据；
 */
public interface TradeRepository extends ReactiveMongoRepository<MatchStream, String> {

}
