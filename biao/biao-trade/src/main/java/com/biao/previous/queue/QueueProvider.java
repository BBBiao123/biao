package com.biao.previous.queue;

import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;


public class QueueProvider<T> {

    /**
     * 缓冲区；
     */
    private final RingBuffer<QueueEvent<T>> ringBuffer;
    private Logger logger = LoggerFactory.getLogger(QueueProvider.class);

    /**
     * 构造器；
     *
     * @param ringBuffer 缓冲区；
     */
    public QueueProvider(RingBuffer<QueueEvent<T>> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 放入一个数据；
     *
     * @param function 数据处理信息；
     */
    public void onData(Consumer<QueueEvent<T>> function) {
        long position = ringBuffer.next();
        try {
            QueueEvent<T> dx = ringBuffer.get(position);
            function.accept(dx);
            ringBuffer.publish(position);
        } catch (Exception ex) {
            logger.error("ex", ex);
        }
    }
}
