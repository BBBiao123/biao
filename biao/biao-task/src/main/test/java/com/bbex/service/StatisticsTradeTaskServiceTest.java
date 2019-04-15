package com.biao.service;

import com.biao.BaseTest;
import com.biao.enums.TradePairEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""(Myth)
 */
public class StatisticsTradeTaskServiceTest extends BaseTest {

    @Autowired
    private StatisticsTradeTaskService statisticsTradeTaskService;

    @Test
    public void statisticsTradeTask() {
    }

    @Test
    public void everyMinForDayTrade() {
        statisticsTradeTaskService.everyMinForDayTrade(TradePairEnum.USDT.getKey());
    }

    @Test
    public void statisticsTradeDay() {
    }

    @Test
    public void userSynTask() {
        statisticsTradeTaskService.userSynTask();
    }

    @Test
    public void sendMessageToSynUser() {
        statisticsTradeTaskService.userSynSendMessageTask();
    }
}