package com.biao.binance;

import com.biao.binance.config.KlinePullConfig;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.current.threadpool.BbexThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * KlinePullDataHandler.
 *
 *  ""
 */
@Component
public class KlinePullDataHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KlinePullDataHandler.class);

    private static BlockingQueue<KlinePullConfig> QUEUE = new LinkedBlockingQueue<>(1024);

    private static final int MAX_THREAD = Runtime.getRuntime().availableProcessors() << 1;

    private final KlineDataService klineDataService;

    /**
     * Instantiates a new Kline pull data handler.
     *
     * @param klineDataService the kline data service
     */
    @Autowired
    public KlinePullDataHandler(final KlineDataService klineDataService) {
        this.klineDataService = klineDataService;
    }


    /**
     * Init.
     */
    @PostConstruct
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


    /**
     * Submit.
     *
     * @param config the config
     */
    public static void submit(final KlinePullConfig config) {
        try {
            QUEUE.put(config);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * The type Worker.
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
