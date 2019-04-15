package com.biao.previous.message;

import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * 一个订阅者；
 *
 *
 */
public interface TradeExecutorSubscriber<T> {
    /**
     * 发送一个通知到处理器；
     *
     * @param collections 数据；
     * @return 数据；
     */
    Flux<String> executor(Collection<? extends T> collections);
}
