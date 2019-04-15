package com.biao.service.kline;

import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.current.threadpool.BbexThreadPool;
import com.biao.entity.kline.KlinePullConfig;
import com.biao.service.KlineDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * KlinePullDataHandler.
 *
 *  ""
 */
public class KlinePullDataHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KlinePullDataHandler.class);

    private static BlockingQueue<KlinePullConfig> QUEUE = new LinkedBlockingQueue<>(1024);

    private static final int MAX_THREAD = Runtime.getRuntime().availableProcessors() << 1;

    private final KlineDataService klineDataService;

    public KlinePullDataHandler(KlineDataService klineDataService) {
        this.klineDataService = klineDataService;
    }

    /**
     * 初始化
     */
    public void init() {
        synchronized (LOGGER) {
            final ExecutorService executorService =
                    BbexThreadPool.newCustomFixedThreadPool(MAX_THREAD,
                            BbexThreadFactory.create("kline-pull-data",
                                    false));
            for (int i = 0; i < MAX_THREAD; i++) {
                executorService.execute(new Worker());
            }
        }
    }


    public static void submit(KlinePullConfig config) {
        try {
            QUEUE.put(config);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 线程执行器
     */
    class Worker implements Runnable {

        @Override
        public void run() {
            execute();
        }

        /**
         * 执行..
         */
        private void execute() {
            while (true) {
                try {
                    KlinePullConfig config = QUEUE.take();
                    klineDataService.handlerPullData(config);
                } catch (Exception e) {
                    LOGGER.error(" failure ," + e.getMessage());
                }
            }
        }
    }
}
