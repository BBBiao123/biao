package com.bbex.robot.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 构造一个线程相关信息；
 *
 * @author p
 */
public class NameThreadFactory implements ThreadFactory {

    /**
     * tg
     */
    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("bbex");
    /**
     * 名称序号；
     */
    private final AtomicLong threadNumber = new AtomicLong(1);
    /**
     * 名称前缀；
     */
    private final String namePrefix;

    /**
     * 文件前缀；
     *
     * @param namePrefix name;
     */
    public NameThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    /**
     * 初始化一个线程；
     *
     * @param runnable r;
     * @return thread;
     */
    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(THREAD_GROUP, runnable,
                THREAD_GROUP.getName() + "-" + namePrefix + "-" + threadNumber.getAndIncrement());
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
