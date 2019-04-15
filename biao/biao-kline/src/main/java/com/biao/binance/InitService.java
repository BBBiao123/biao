package com.biao.binance;

import com.biao.binance.config.KlinePullConfig;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.dao.KlinePullConfigDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  ""(Myth)
 */
@Component
public class InitService implements InitializingBean {

    private final KlinePullConfigDao klinePullConfigDao;

    private final ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(1,
                    BbexThreadFactory.create("binnace-kline-task", false));

    @Autowired(required = false)
    public InitService(final KlinePullConfigDao klinePullConfigDao) {
        this.klinePullConfigDao = klinePullConfigDao;
    }

    @Override
    public void afterPropertiesSet() {
        scheduledExecutorService
                .scheduleWithFixedDelay(() -> {
                    try {
                        final List<KlinePullConfig> klinePullConfigs = klinePullConfigDao.findAll();
                        if (CollectionUtils.isNotEmpty(klinePullConfigs)) {
                            klinePullConfigs.forEach(KlinePullDataHandler::submit);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 30, 60, TimeUnit.SECONDS);
    }
}
