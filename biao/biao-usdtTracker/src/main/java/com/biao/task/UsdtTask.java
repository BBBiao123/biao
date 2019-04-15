package com.biao.task;

import com.biao.collect.CollectionService;
import com.biao.config.OmniConfig;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.service.OmniService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * SyncBlockJob.
 */
@Component
@SuppressWarnings("unchecked")
public class UsdtTask implements CommandLineRunner {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UsdtTask.class);

    private final OmniService omniService;

    private final CollectionService collectionService;

    private final OmniConfig omniConfig;

    private ScheduledExecutorService syncScheduled =
            new ScheduledThreadPoolExecutor(1,
                    BbexThreadFactory.create("usdt-sync-task", false));


    private ScheduledExecutorService sendScheduled =
            new ScheduledThreadPoolExecutor(1,
                    BbexThreadFactory.create("usdt-send-task", false));

    private ScheduledExecutorService collectionScheduled =
            new ScheduledThreadPoolExecutor(1,
                    BbexThreadFactory.create("usdt-collect-task", false));


    private ScheduledExecutorService addrScheduled =
            new ScheduledThreadPoolExecutor(1,
                    BbexThreadFactory.create("usdt-addr-task", false));

    @Autowired
    public UsdtTask(final OmniService omniService,
                    final CollectionService collectionService,
                    final OmniConfig omniConfig) {
        this.omniService = omniService;
        this.collectionService = collectionService;
        this.omniConfig = omniConfig;
    }

    @Override
    public void run(final String... args) {
        syncScheduled
                .scheduleWithFixedDelay(() -> {
                    LOGGER.info("定时执行usdt 区块同步任务，同步间隔时间为：{} 秒", omniConfig.getSyncTime());
                    try {
                        omniService.syncBlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 10, omniConfig.getSyncTime(), TimeUnit.SECONDS);


        sendScheduled
                .scheduleWithFixedDelay(() -> {
                    LOGGER.info("定时执行usdt 提现任务 ，间隔时间为：{} 秒", omniConfig.getSendTime());
                    try {
                        omniService.usdtSendTask();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 1, omniConfig.getSendTime(), TimeUnit.SECONDS);

        collectionScheduled
                .scheduleWithFixedDelay(() -> {
                    LOGGER.info("定时执行usdt归集任务 ，同步间隔时间为：{} 秒", omniConfig.getCollectTime());
                    try {
                        collectionService.collectTask();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 1, omniConfig.getCollectTime(), TimeUnit.SECONDS);


        collectionScheduled
                .scheduleWithFixedDelay(() -> {
                    LOGGER.info("定时执行生成usdt地址 ，同步间隔时间为：{} 秒", 300);
                    try {
                        omniService.executeAddress();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 1, 300, TimeUnit.SECONDS);


    }


}
