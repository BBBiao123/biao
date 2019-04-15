package com.biao.previous.message;

import com.biao.reactive.data.mongo.domain.TradeDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * 把详情写入的到mongo中；
 *
 * ""sixh
 */
public class TradeDetailConsumerByDetail implements TradeExecutorSubscriber<TradeDetail> {
    /**
     * 数据库操作类；
     */
    private ReactiveMongoTemplate mongoTemplate;
    /**
     * 日志打印;
     */
    private Logger logger = LoggerFactory.getLogger(TradeDetailConsumerByDetail.class);

    /**
     * 初始化mongo数据;
     *
     * @param mongoTemplate spring对象;
     */
    public TradeDetailConsumerByDetail(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<String> executor(Collection<? extends TradeDetail> collections) {
        return mongoTemplate
                .insertAll(collections)
                .doOnError(e -> logger.error("更新mongodb失败了{}", e))
                .flatMap(e -> Flux.just("DETAIL_插入流水(Detail)数据到mongodb成功!订单号：" + e.getOrderNo() + "交易号：" + e.getTradeNo()));
    }
}
