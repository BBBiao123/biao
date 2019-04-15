package com.biao.previous.message;

import com.biao.previous.domain.CancelResult;
import com.biao.previous.domain.TrParent;
import com.biao.previous.domain.TrType;
import com.biao.previous.domain.TradeResult;
import com.biao.previous.queue.QueueConsumerExecutor;
import com.biao.previous.queue.QueueConsumerFactory;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * 消息结果处理器；
 *
 *
 */
@SuppressWarnings("all")
public class QueueTradeResultExecutor extends QueueConsumerExecutor<List<TrParent>> {

    private EnumMap<TrType, Set<TrExecutorSubscriber>> overSubscribers = new EnumMap<TrType, Set<TrExecutorSubscriber>>(TrType.class);

    /**
     * Instantiates a new Queue trade result executor.
     *
     * @param overSubscribers the over subscribers
     */
    public QueueTradeResultExecutor(Map<TrType, Set<TrExecutorSubscriber>> overSubscribers) {
        this.overSubscribers.putAll(overSubscribers);
    }

    @Override
    public void run() {
        List<TrParent> results = getData();
        Set<TrExecutorSubscriber> subscribers = getType(results);
        Flux.fromIterable(subscribers).subscribe(tradeExecutorSubscriber ->
                tradeExecutorSubscriber
                        .executor(results)
                        .doOnError(ie -> logger.error("处理结果失败了{}", ie))
                        .subscribe(e -> logger.info("{}", e)));
    }

    private Set<TrExecutorSubscriber> getType(List<TrParent> list) {
        if(list == null || list.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        TrParent trParent = list.get(0);
        if(trParent instanceof CancelResult) {
           return overSubscribers.get(TrType.CANCEL);
        }else  if(trParent instanceof TradeResult) {
            return overSubscribers.get(TrType.TRADE);
        }
        return Collections.EMPTY_SET;
    }
    /**
     * 工厂初始化;
     */
    public static class QueueTradeResultExecutorFactory extends QueueConsumerFactory.AbstractQueueConsumerFactory {

        @Override
        public QueueConsumerExecutor create() {
            Map<TrType, Set<TrExecutorSubscriber>> collect = subscribers.stream().map(e -> {
                return (TrExecutorSubscriber) e;
            }).collect(Collectors.groupingBy(e -> e.getType(), toSet()));
            return new QueueTradeResultExecutor(collect).addSubscribers(subscribers);
        }

        @Override
        public String fixName() {
            return "order_result";
        }

    }
}
