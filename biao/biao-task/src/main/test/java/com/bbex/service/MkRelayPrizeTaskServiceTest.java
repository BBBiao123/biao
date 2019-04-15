package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""
 */
public class MkRelayPrizeTaskServiceTest extends BaseTest {

    @Autowired
    private MkRelayPrizeTaskService mkRelayPrizeTaskService;

    @Test
    public void testRelayPrizeTaskEntry() {
        mkRelayPrizeTaskService.relayPrizeTaskEntry();
    }

    @Test
    public void testRelayAutoTaskEntry() {
        mkRelayPrizeTaskService.relayAutoTaskEntry();
    }
}