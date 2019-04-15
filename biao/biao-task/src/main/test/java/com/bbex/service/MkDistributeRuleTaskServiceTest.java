package com.biao.service;

import com.biao.BaseTest;
import com.biao.service.impl.MkCommonTaskServiceImpl;
import com.biao.service.impl.MkDividendRuleTaskServiceImpl;
import com.biao.service.impl.MkPromoteRuleTaskServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""
 */
public class MkDistributeRuleTaskServiceTest extends BaseTest {

    @Autowired
    private MkRuleTaskCoreService mkRuleTaskCoreService;

    @Autowired
    private MkDividendRuleTaskServiceImpl mkDividendRuleTaskService;

    @Autowired
    private MkPromoteRuleTaskServiceImpl mkPromoteRuleTaskService;

    @Autowired
    private MkCommonTaskServiceImpl mkCommonTaskService;

    @Test
    public void testTriggerMiningDayTask() {
        mkRuleTaskCoreService.triggerMiningDayTask();
    }

    @Test
    public void testTriggerDividendDayTask() {
        mkRuleTaskCoreService.triggerDividendDayTask();
    }

    @Test
    public void testTriggerPromoteDayTask() {
        mkRuleTaskCoreService.triggerPromoteDayTask();
    }

    @Test
    public void testTakeUserCoinVolumeSnapshot() {
        mkDividendRuleTaskService.triggerUserCoinVolumeSnapshot();
    }

    @Test
    public void testTriggerRemitFeeToPlatAccount() {
        mkDividendRuleTaskService.triggerRemitFeeToPlatAccount();
    }

    @Test
    public void testInitPlatUserRelation() {
        mkPromoteRuleTaskService.initPlatUserRelation();
    }

    @Test
    public void testCalcUserMainCoinFee() {
        mkCommonTaskService.calcUserMainCoinFee();
    }
}