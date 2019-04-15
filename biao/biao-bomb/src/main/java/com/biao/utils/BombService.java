package com.biao.utils;

import com.biao.service.MkAutoTradeTaskService;
import com.biao.service.impl.MkAutoTradeTaskServiceImpl;
import com.biao.spring.SpringBeanFactoryContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;

public class BombService {

    public static Logger logger = LoggerFactory.getLogger(BombService.class);

    public static void start() {

        clearMonitorRedis();

        triggerHandleActiveMonitor();

        triggerInitAutoTrade();
    }

    public static void triggerHandleActiveMonitor() {
        logger.info("重新加载启动中的监控！");
        MkAutoTradeTaskService mkAutoTradeTaskService = (MkAutoTradeTaskService) SpringBeanFactoryContext.findBean(MkAutoTradeTaskServiceImpl.class);
        mkAutoTradeTaskService.handleActiveMonitor();
    }

    public static void triggerInitAutoTrade() {
        logger.info("重新加载自动交易配置！");
        Timer timer = new Timer();
        BombTask bombTask = new BombTask(timer);
        timer.schedule(bombTask, new Date(), 60 * 1000);
    }

    public static void clearMonitorRedis() {
        logger.info("清除redis缓存.....");
        MkAutoTradeTaskService mkAutoTradeTaskService = (MkAutoTradeTaskService) SpringBeanFactoryContext.findBean(MkAutoTradeTaskServiceImpl.class);
        mkAutoTradeTaskService.clearMonitorRedis();
    }
}
