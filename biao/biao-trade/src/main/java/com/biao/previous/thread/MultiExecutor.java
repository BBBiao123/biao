package com.biao.previous.thread;

import com.biao.current.threadpool.BbexThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 *  ""
 */
public class MultiExecutor extends ThreadPoolExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiExecutor.class);
    private static final int QUEUE_SIZE = 5000;
    private static final RejectedExecutionHandler HANDLER = (r, executor) -> {
        BlockingQueue<Runnable> queue = executor.getQueue();
        while (queue.size() >= QUEUE_SIZE) {
            if (executor.isShutdown()) {
                throw new RejectedExecutionException("DebuggableThreadPoolExecutor 已经关闭");
            }
            try {
                ((MultiExecutor) executor).onRejected();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
        executor.execute(r);
    };
    /**
     * 名称
     */
    private String name;

    private void onRejected() {
        LOGGER.error("线程{},发生饱和,执行器{}", Thread.currentThread().getName(), name);
    }

    /**
     * 初始化多端线程池;
     *
     * @param coreSize 核心数据;
     * @param maxSize  最大核心数;
     * @param poolName 线程名字;
     */
    public MultiExecutor(int coreSize, int maxSize, String poolName) {
        super(coreSize, maxSize, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(QUEUE_SIZE),
                BbexThreadFactory.create(poolName, false),
                HANDLER);
        this.name = poolName;
    }

    public String getName() {
        return name;
    }
}