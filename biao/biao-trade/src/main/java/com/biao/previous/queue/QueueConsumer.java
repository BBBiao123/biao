package com.biao.previous.queue;

import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 */
public class QueueConsumer<T> implements WorkHandler<QueueEvent<T>> {

    /**
     * 线程池执行器；
     */
    private ThreadPoolExecutor executor;
    /**
     * 创建的对象;
     */
    private QueueConsumerFactory factory;

    /**
     * 消息设置；
     *
     * @param executor 处理器；
     * @param factory  线程处理对象；
     */
    public QueueConsumer(ThreadPoolExecutor executor, QueueConsumerFactory factory) {
        this.executor = executor;
        this.factory = factory;
    }

    @Override
    public void onEvent(QueueEvent<T> t) {
        if (t != null) {
            QueueConsumerExecutor<T> runnable = factory.create();
            runnable.setData(t.getT());
            executor.execute(runnable);
        }
    }
}
