package com.biao.previous.queue;

import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.previous.thread.MultiExecutor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 管理器；
 *
 */
public class QueueProviderManage<T> {

    /**
     * 分区大小；
     */
    private final Integer size;

    /**
     * 处理器；
     */
    private QueueConsumerFactory queueConsumerFactory;

    /**
     * 线程组处理器；
     */
    private ThreadPoolExecutor executor;
    /**
     * 提供者;
     */
    private QueueProvider<T> provider;
    /**
     * 线程与客户端的大小;
     */
    private Integer threadSize;

    /**
     * 处理;
     *
     * @param queueConsumerFactory 处理；
     */
    public QueueProviderManage(QueueConsumerFactory queueConsumerFactory) {
        this(queueConsumerFactory, 4096 * 2 * 2);
    }

    /**
     * 初始化数据;
     */
    public QueueProviderManage(QueueConsumerFactory queueConsumerFactory, int ringBufferSize) {
        this(queueConsumerFactory, Runtime.getRuntime().availableProcessors()+1, ringBufferSize);
    }

    /**
     * 初始化数据;
     */
    public QueueProviderManage(QueueConsumerFactory queueConsumerFactory, int threadSize, int ringBufferSize) {
        this.queueConsumerFactory = queueConsumerFactory;
        this.threadSize = threadSize;
        this.size = ringBufferSize;
        executor = new MultiExecutor(threadSize, threadSize, "trade_queue_task" + queueConsumerFactory.fixName());
    }

    /**
     * 启动数据；
     */
    public void start() {
        QueueEventFactory factory = new QueueEventFactory();
        /*
         * 处理器；
         */
        Disruptor<T> disruptor = new Disruptor<T>(factory,
                size,
                BbexThreadFactory.create("trade_queue_" + queueConsumerFactory.fixName(), false),
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        QueueConsumer[] consumers = new QueueConsumer[this.threadSize];
        for (int i = 0; i < this.threadSize; i++) {
            consumers[i] = new QueueConsumer(executor, queueConsumerFactory);
        }
        disruptor.handleEventsWithWorkerPool(consumers);
        disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());
        disruptor.start();
        RingBuffer buffer = disruptor.getRingBuffer();
        provider = new QueueProvider(buffer);
    }

    /**
     * 得到当前的提供者；
     *
     * @return 返回提供者；
     */
    public QueueProvider<T> getProvider() {
        return provider;
    }
}
