package com.biao.service;

public interface MkAutoTradeTaskService {

    void triggerAutoTradeTask();

    void handleActiveMonitor();

    void initAutoTrade();

    void clearMonitorRedis();
}
