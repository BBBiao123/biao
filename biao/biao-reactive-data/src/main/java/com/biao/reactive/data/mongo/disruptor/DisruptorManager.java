package com.biao.reactive.data.mongo.disruptor;

import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class DisruptorManager {

    private static DisruptorManager manager = new DisruptorManager();

    private Disruptor<DisruptorData> disruptor;

    private int ringBufferSize;

    private List<DisruptorDataEventHandler> dataEventHandlers = new ArrayList<DisruptorDataEventHandler>();

    private AtomicBoolean start = new AtomicBoolean(false);

    public static DisruptorManager instance() {
        return manager;
    }

    private DisruptorManager() {

    }

    public void init() {
        if (start.compareAndSet(false, true)) {
            if (ringBufferSize < 1) {
                ringBufferSize = 1;
            }
            //默认采用阻塞模式和多生产者
            disruptor = new Disruptor<>(DisruptorData.FACTORY_INSTANCE, 2 * ringBufferSize, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r);
                }
            });
            if (dataEventHandlers.size() < 1) {
                throw new IllegalArgumentException("consumer is null");
            }
            DisruptorDataEventHandler[] arrays = new DisruptorDataEventHandler[dataEventHandlers.size()];
            dataEventHandlers.toArray(arrays);
            disruptor.handleEventsWithWorkerPool(arrays);
            disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());
            disruptor.start();
        }
    }

    public boolean getStart() {
        return start.get();
    }

    public Disruptor<DisruptorData> getDisruptor() {
        return disruptor;
    }

    public RingBuffer<DisruptorData> getRingBuffer() {
        return disruptor.getRingBuffer();
    }

    public void addDataEventHandler(DisruptorDataEventHandler dataEventHandler) {
        this.dataEventHandlers.add(dataEventHandler);
    }

    /**
     * 真实大小为设置的2倍
     *
     * @param ringBufferSize
     */
    public void setRingBufferSize(int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
    }

    /**
     * 数据 必须包含type属性,用于区分数据类型
     *
     * @param data
     */
    public void publishData(DisruptorData data) {
        long sequence = disruptor.getRingBuffer().next();
        try {
            DisruptorData disruptorData = disruptor.getRingBuffer().get(sequence);
            //DisruptorData converData = JsonUtils.fromJson(JsonUtils.toJson(data), DisruptorData.class);
            BeanUtils.copyProperties(data, disruptorData);
        } finally {
            disruptor.getRingBuffer().publish(sequence);
        }
    }

    public DisruptorDataEventHandler[] createDisruptorDataEventHandlers(int size) {
        DisruptorDataEventHandler[] arrays = new DisruptorDataEventHandler[size];
        for (int i = 0; i < size; i++) {
            arrays[i] = new DisruptorDataEventHandler();
        }
        return arrays;
    }

    /**
     * 默认创建ringBuffer的数量和消费者的数量
     *
     * @param ringBufferSize         1024
     * @param dataEventHandlerArrays 3
     */
    public DisruptorManager runConfig() {
        return runConfig(512, 3);
    }

    public DisruptorManager runConfig(int ringBufferSize, int dataEventHandlerArrays) {
        //初始化
        if (dataEventHandlerArrays < 1) {
            dataEventHandlerArrays = 1;
        }
        return runConfig(ringBufferSize, createDisruptorDataEventHandlers(dataEventHandlerArrays));
    }

    public DisruptorManager runConfig(int ringBufferSize, DisruptorDataEventHandler... dataEventHandlerArrays) {
        if (!manager.getStart()) {
            //初始化
            this.ringBufferSize = ringBufferSize;
            Stream.of(dataEventHandlerArrays).forEach(dataEventHandlerArray -> manager.addDataEventHandler(dataEventHandlerArray));
            manager.init();
        }
        return this;
    }

    public void close() {
        disruptor.shutdown();
        dataEventHandlers.clear();
        start.set(false);
    }
}
