package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""
 */
public class PlatUserRelationServiceTest extends BaseTest {

    @Autowired
    private PlatUserRelationService platUserRelationService;

    @Autowired
    private PlatUserSynaService platUserSynaService;

    @Autowired
    private StatisticsTradeTaskService statisticsTradeTaskService;

    @Test
    public void testInitPlatUserRelation(){ platUserRelationService.initPlatUserRelation(); }

    @Test
    public void testSynEgoToPlatUser(){ platUserSynaService.synEgoToPlatUser(); }

    @Test
    public void testUserSynSendMessageTask(){
        statisticsTradeTaskService.userSynSendMessageTask();
    }


}