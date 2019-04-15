package com.biao.service;

/**
 *  ""
 */
public interface MkRuleTaskCoreService {

    /**
     * 每天触发挖矿规则
     */
    void triggerMiningDayTask();

    /**
     * 每天触发分红规则
     */
    void triggerDividendDayTask();

    /**
     * 每天触发会员推广
     */
    void triggerPromoteDayTask();


}
