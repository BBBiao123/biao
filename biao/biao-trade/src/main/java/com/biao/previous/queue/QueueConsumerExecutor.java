package com.biao.previous.queue;

import com.biao.previous.message.TradeExecutorSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public abstract class QueueConsumerExecutor<T> implements Runnable {
    protected Logger logger = LoggerFactory.getLogger("metrics");
    /**
     * 数据处理；
     */
    private T data;
    /**
     * 记录了用户需要订阅计算结果后的订阅处理；
     */
    protected Set<TradeExecutorSubscriber> subscribers = new HashSet<>();

    /**
     * 增加一个处理器；
     *
     * @param subscriber 订阅的处理器；
     * @return 返回当
     */
    public QueueConsumerExecutor addSubscribers(TradeExecutorSubscriber subscriber) {
        subscribers.add(subscriber);
        return this;
    }

    /**
     * 增加一个处理器；
     *
     * @param subscribers 订阅的处理器；
     * @return 返回当
     */
    public QueueConsumerExecutor addSubscribers(Set<TradeExecutorSubscriber> subscribers) {
        subscribers.forEach(this::addSubscribers);
        return this;
    }

    /**
     * 获取数据；
     *
     * @return 数据；
     */
    public T getData() {
        return data;
    }

    /**
     * 设置一个数据;
     *
     * @param data data;
     */
    public void setData(T data) {
        this.data = data;
    }
}
