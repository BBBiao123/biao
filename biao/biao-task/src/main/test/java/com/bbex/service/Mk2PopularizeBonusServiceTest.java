package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class Mk2PopularizeBonusServiceTest extends BaseTest {

    @Autowired
    private Mk2PopularizeBonusService mk2PopularizeBonusService;


    @Autowired
    private StatisticsTradeTaskService statisticsTradeTaskService;

    @Test
    public void testBonus() {
        mk2PopularizeBonusService.doPopularizeBonusTask();
    }

    @Test
    public void testPhone() {
        statisticsTradeTaskService.userGeoOneTask();
    }

}
