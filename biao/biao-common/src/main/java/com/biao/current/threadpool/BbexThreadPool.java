/*
 *
 * Copyright 2017-2018 ""611@qq.com(ttt)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.biao.current.threadpool;


import com.biao.current.threadpool.policy.AbortPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 *  ""
 */
public class BbexThreadPool {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BbexThreadPool.class);

    private static final String THREAD_FACTORY_NAME = "biao";
    private static final int MAX_ARRAY_QUEUE = 1000;

    public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() << 1;


    private static final ScheduledExecutorService SCHEDULED_THREAD_POOL_EXECUTOR =
            new ScheduledThreadPoolExecutor(1,
                    BbexThreadFactory.create(THREAD_FACTORY_NAME, true));


    public static ExecutorService newCustomFixedThreadPool(int threads) {
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                BbexThreadFactory.create(THREAD_FACTORY_NAME, false), new AbortPolicy());
    }


    public static ExecutorService newCustomFixedThreadPool(int threads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory, new AbortPolicy());
    }

    public static ExecutorService newSingleThreadExecutor() {
        return new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                BbexThreadFactory.create(THREAD_FACTORY_NAME, false), new AbortPolicy());
    }

    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory, new AbortPolicy());
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
        return SCHEDULED_THREAD_POOL_EXECUTOR;
    }

}

