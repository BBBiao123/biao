package com.biao.previous.thread;

import com.biao.current.threadpool.BbexThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 *  ""
 */
public class SingletonExecutor extends ThreadPoolExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonExecutor.class);
    private static final int QUEUE_SIZE = 5000;
    private static final RejectedExecutionHandler HANDLER = (r, executor) -> {
        BlockingQueue<Runnable> queue = executor.getQueue();
        while (queue.size() >= QUEUE_SIZE) {
            if (executor.isShutdown()) {
                throw new RejectedExecutionException("DebuggableThreadPoolExecutor 已经关闭");
            }
            try {
                ((SingletonExecutor) executor).onRejected();
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

    public SingletonExecutor(String poolName) {
        super(1, 1, 0L,
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