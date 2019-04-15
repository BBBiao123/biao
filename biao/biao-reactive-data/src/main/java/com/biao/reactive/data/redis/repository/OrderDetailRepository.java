package com.biao.reactive.data.redis.repository;

import com.biao.reactive.data.mongo.domain.OrderDetail;
import org.springframework.data.repository.CrudRepository;

/**
 * 撮合详情表  比如一个买单是由几笔构成
 */
public interface OrderDetailRepository extends CrudRepository<OrderDetail, String> {
}
