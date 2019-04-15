package com.biao.kline.disruptor;

import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * The type Disruptor manager.
 */
public class DisruptorManager {

    private static DisruptorManager manager = new DisruptorManager();

    private Disruptor<DisruptorData> disruptor;

    private int ringBufferSize;

    private static final Integer availableProcessors = Runtime.getRuntime().availableProcessors() << 1;

    private static final Integer MAX_POOL = 2048;

    private List<DisruptorDataEventHandler> dataEventHandlers = new ArrayList<DisruptorDataEventHandler>();

    private AtomicBoolean start = new AtomicBoolean(false);

    /**
     * Instance disruptor manager.
     *
     * @return the disruptor manager
     */
    public static DisruptorManager instance() {
        return manager;
    }

    private DisruptorManager() {

    }

    /**
     * Init.
     */
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

    /**
     * Gets start.
     *
     * @return the start
     */
    public boolean getStart() {
        return start.get();
    }

    /**
     * Gets disruptor.
     *
     * @return the disruptor
     */
    public Disruptor<DisruptorData> getDisruptor() {
        return disruptor;
    }

    /**
     * Gets ring buffer.
     *
     * @return the ring buffer
     */
    public RingBuffer<DisruptorData> getRingBuffer() {
        return disruptor.getRingBuffer();
    }

    /**
     * Add data event handler.
     *
     * @param dataEventHandler the data event handler
     */
    public void addDataEventHandler(DisruptorDataEventHandler dataEventHandler) {
        this.dataEventHandlers.add(dataEventHandler);
    }

    /**
     * 真实大小为设置的2倍
     *
     * @param ringBufferSize the ring buffer size
     */
    public void setRingBufferSize(int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
    }

    /**
     * 数据 必须包含type属性,用于区分数据类型
     *
     * @param data the data
     */
    public void publishData(DisruptorData data) {
        long sequence = disruptor.getRingBuffer().next();
        try {
            DisruptorData disruptorData = disruptor.getRingBuffer().get(sequence);
            BeanUtils.copyProperties(data, disruptorData);
        } finally {
            disruptor.getRingBuffer().publish(sequence);
        }
    }

    /**
     * Create disruptor data event handlers disruptor data event handler [ ].
     *
     * @param size the size
     * @return the disruptor data event handler [ ]
     */
    public DisruptorDataEventHandler[] createDisruptorDataEventHandlers(int size) {
        DisruptorDataEventHandler[] arrays = new DisruptorDataEventHandler[size];
        for (int i = 0; i < size; i++) {
            arrays[i] = new DisruptorDataEventHandler();
        }
        return arrays;
    }

    /**
     * Publish disruptor.
     *
     * @param data the data
     * @param type the type
     */
    public static void publishDisruptor(Object data, Integer type) {
        DisruptorData disruptorData = new DisruptorData();
        disruptorData.setType(type);
        disruptorData.setData(data);
        DisruptorManager.publishDisruptor(disruptorData);
    }

    /**
     * Publish disruptor.
     *
     * @param disruptorData the disruptor data
     */
    public static void publishDisruptor(DisruptorData disruptorData) {
        DisruptorManager.instance().runConfig().publishData(disruptorData);
    }


    /**
     * Run config disruptor manager.
     *
     * @return the disruptor manager
     */
    public DisruptorManager runConfig() {
        return runConfig(MAX_POOL, availableProcessors);
    }

    /**
     * Run config disruptor manager.
     *
     * @param ringBufferSize         the ring buffer size
     * @param dataEventHandlerArrays the data event handler arrays
     * @return the disruptor manager
     */
    public DisruptorManager runConfig(int ringBufferSize, int dataEventHandlerArrays) {
        //初始化
        if (dataEventHandlerArrays < 1) {
            dataEventHandlerArrays = 1;
        }
        return runConfig(ringBufferSize, createDisruptorDataEventHandlers(dataEventHandlerArrays));
    }

    /**
     * Run config disruptor manager.
     *
     * @param ringBufferSize         the ring buffer size
     * @param dataEventHandlerArrays the data event handler arrays
     * @return the disruptor manager
     */
    public DisruptorManager runConfig(int ringBufferSize, DisruptorDataEventHandler... dataEventHandlerArrays) {
        if (!manager.getStart()) {
            //初始化
            this.ringBufferSize = ringBufferSize;
            Stream.of(dataEventHandlerArrays).forEach(dataEventHandlerArray -> manager.addDataEventHandler(dataEventHandlerArray));
            manager.init();
        }
        return this;
    }

    /**
     * Close.
     */
    public void close() {
        disruptor.shutdown();
        dataEventHandlers.clear();
        start.set(false);
    }
}
