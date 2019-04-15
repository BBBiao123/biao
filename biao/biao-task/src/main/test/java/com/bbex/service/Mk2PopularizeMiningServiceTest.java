package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Mk2PopularizeMiningServiceTest extends BaseTest {

    @Autowired
    private Mk2PopularizeMiningService mk2PopularizeMiningService;

    @Autowired
    private Mk2MiningTeamSortTaskService mk2MiningTeamSortTaskService;

    @Autowired
    private PlatUserCoinCountService platUserCoinCountService;

    @Test
    public void testMining() {
        mk2PopularizeMiningService.doPopularizeMining();
    }

    @Test
    public void testUserHoldCoin() {
        platUserCoinCountService.countUserCoin();
    }

    @Test
    public void testMiningSort() {
        mk2MiningTeamSortTaskService.doSortTeamMinging();
    }
}
