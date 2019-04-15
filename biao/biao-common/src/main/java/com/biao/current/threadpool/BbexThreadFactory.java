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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The type Bbex thread factory.
 *
 *  ""
 */
public class BbexThreadFactory implements ThreadFactory {

    private static final AtomicLong THREAD_NUMBER = new AtomicLong(1);

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("biao");

    private boolean daemon;

    private final String namePrefix;

    /**
     * Instantiates a new Bbex thread factory.
     *
     * @param namePrefix the name prefix
     * @param daemon     the daemon
     */
    public BbexThreadFactory(final String namePrefix, final boolean daemon) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
    }

    /**
     * Instantiates a new Bbex thread factory.
     *
     * @param namePrefix the name prefix
     */
    public BbexThreadFactory(final String namePrefix) {
        this(namePrefix, false);
    }

    /**
     * create custom thread factory.
     *
     * @param namePrefix prefix
     * @param daemon     daemon
     * @return {@linkplain ThreadFactory}
     */
    public static ThreadFactory create(final String namePrefix, final boolean daemon) {
        return new BbexThreadFactory(namePrefix, daemon);
    }

    /**
     * create custom thread factory.
     *
     * @param namePrefix prefix
     * @return {@linkplain ThreadFactory}
     */
    public static ThreadFactory create(final String namePrefix) {
        return create(namePrefix, false);
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        Thread thread = new Thread(THREAD_GROUP, runnable,
                THREAD_GROUP.getName() + "-" + namePrefix + "-" + THREAD_NUMBER.getAndIncrement());
        thread.setDaemon(daemon);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
