package com.biao.service.kline;

import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.current.threadpool.BbexThreadPool;
import com.biao.service.KlineLogDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class KlineLogDataHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KlineLogDataHandler.class);

    private static BlockingQueue<KlineLogDataConfig> QUEUE = new LinkedBlockingQueue<>(1024);

    private static final int MAX_THREAD = Runtime.getRuntime().availableProcessors() << 1;

    private final KlineLogDataService klineLogDataService;

    @Autowired
    public KlineLogDataHandler(KlineLogDataService klineLogDataService) {
        this.klineLogDataService = klineLogDataService;
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        synchronized (LOGGER) {
            final ExecutorService executorService =
                    BbexThreadPool.newCustomFixedThreadPool(MAX_THREAD,
                            BbexThreadFactory.create("kline-log-data",
                                    false));
            for (int i = 0; i < MAX_THREAD; i++) {
                executorService.execute(new Worker());
            }
        }
    }


    public static void submit(KlineLogDataConfig config) {
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
                    KlineLogDataConfig config = QUEUE.take();
                    if(config.isKlineInitTag()) {
                    	klineLogDataService.initKlineData(config);
                    	continue ;
                    }
                    if(config.isSynRedis()) {
                    	klineLogDataService.synRedis(config);
                    }else {
                    	klineLogDataService.klineLogData(config, true);
                    }
                } catch (Exception e) {
                    LOGGER.error(" failure ," + e.getMessage());
                }
            }
        }
    }
}
