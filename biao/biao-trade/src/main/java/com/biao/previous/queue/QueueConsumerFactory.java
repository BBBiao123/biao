package com.biao.previous.queue;

import com.biao.previous.message.TradeExecutorSubscriber;

import java.util.HashSet;
import java.util.Set;

/**
 *  ""
 */
public interface QueueConsumerFactory {
    /**
     * 创建一个对象;
     *
     * @return 返回一个对象;
     */
    QueueConsumerExecutor create();

    /**
     * 获取一个可以标识的fixName;
     */
    String fixName();


    abstract class AbstractQueueConsumerFactory implements QueueConsumerFactory {
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
        public AbstractQueueConsumerFactory addSubscribers(TradeExecutorSubscriber subscriber) {
            subscribers.add(subscriber);
            return this;
        }
    }
}
