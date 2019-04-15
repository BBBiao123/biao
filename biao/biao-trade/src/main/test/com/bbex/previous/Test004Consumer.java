package com.biao.previous;

import com.biao.previous.queue.QueueConsumerExecutor;
import com.biao.previous.queue.QueueConsumerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 关于消息的处理数据；
 *
 *
 */
@SuppressWarnings("all")
public class Test004Consumer extends QueueConsumerExecutor<String> {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据:" + getData());
    }

    /**
     * 工厂初始化;
     */
    public static class Test004ConsumerFactory extends QueueConsumerFactory.AbstractQueueConsumerFactory {
        @Override
        public QueueConsumerExecutor create() {
            return new Test004Consumer().addSubscribers(subscribers);
        }

        @Override
        public String fixName() {
            return "trade_detail";
        }
    }
}