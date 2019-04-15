package com.biao.utils;

import com.biao.service.MkAutoTradeTaskService;
import com.biao.service.impl.MkAutoTradeTaskServiceImpl;
import com.biao.spring.SpringBeanFactoryContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class BombTask extends TimerTask {

    Logger logger = LoggerFactory.getLogger(BombTask.class);
    private Timer timer;

    private MkAutoTradeTaskService mkAutoTradeTaskService;

    public BombTask(Timer timer) {
        this.timer = timer;
        this.mkAutoTradeTaskService = (MkAutoTradeTaskService) SpringBeanFactoryContext.findBean(MkAutoTradeTaskServiceImpl.class);
    }

    @Override
    public void run() {
        logger.info("start....装载自动交易......");
        mkAutoTradeTaskService.initAutoTrade();
        logger.info("end....装载自动交易......");
    }
}
