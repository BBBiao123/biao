package com.biao.kline.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPoolUtils {

    private volatile static AtomicBoolean expairInstance = new AtomicBoolean(false);

    private static ConsistentHashSelector expairSelector = null;

    public static ConsistentHashSelector getExpairInstance() {
        if (expairSelector != null) {
            return expairSelector;
        }
        synchronized (expairInstance) {
            if (expairInstance.compareAndSet(false, true)) {
                List<SingletonExecutor> selects = new ArrayList<>();
                int threads = Runtime.getRuntime().availableProcessors() << 1;
                for (int i = 0; i < threads; i++) {
                    selects.add(new SingletonExecutor("kline_consumer_exe" + i));
                }
                expairSelector = new ConsistentHashSelector(selects);
            }
        }
        return expairSelector;
    }


    private volatile static AtomicBoolean klineInstance = new AtomicBoolean(false);

    private static ConsistentHashSelector klineSelector = null;

    public static ConsistentHashSelector getKlineInstance() {
        if (klineSelector != null) {
            return klineSelector;
        }
        synchronized (klineInstance) {
            if (klineInstance.compareAndSet(false, true)) {
                List<SingletonExecutor> selects = new ArrayList<>();
                int threads = Runtime.getRuntime().availableProcessors() << 1;
                for (int i = 0; i < threads; i++) {
                    selects.add(new SingletonExecutor("kline_handler_exe" + i));
                }
                klineSelector = new ConsistentHashSelector(selects);
            }
        }
        return klineSelector;
    }
}
