package com.biao.previous.message;

import com.biao.previous.queue.QueueConsumerExecutor;
import com.biao.previous.queue.QueueConsumerFactory;
import com.biao.reactive.data.mongo.domain.TradeDetail;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 关于消息的处理数据；
 *
 *
 */
@SuppressWarnings("all")
public class QueueTradeDetailExecutor extends QueueConsumerExecutor<List<TradeDetail>> {

    @Override
    public void run() {
        List<TradeDetail> results = getData();
        Flux.fromIterable(subscribers).subscribe(tradeExecutorSubscriber ->
                tradeExecutorSubscriber
                        .executor(results)
                        .doOnError(e -> {
                            logger.error("更新数据失败了:{}", e);
                        })
                        .subscribe(e -> logger.info("{}", e)));
    }

    /**
     * 工厂初始化;
     */
    public static class QueueTradeDetailExecutorFactory extends QueueConsumerFactory.AbstractQueueConsumerFactory {
        @Override
        public QueueConsumerExecutor create() {
            return new QueueTradeDetailExecutor().addSubscribers(subscribers);
        }

        @Override
        public String fixName() {
            return "trade_detail";
        }
    }
}
