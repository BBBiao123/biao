package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MkAutoTradeTaskTest extends BaseTest {

    @Autowired
    private MkAutoTradeTaskService mkAutoTradeTaskService;

    @Test
    public void testTriggerAutoTradeTask() {
        mkAutoTradeTaskService.triggerAutoTradeTask();
    }
}
