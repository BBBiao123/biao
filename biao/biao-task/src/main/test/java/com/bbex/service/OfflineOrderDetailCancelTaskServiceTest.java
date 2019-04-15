package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OfflineOrderDetailCancelTaskServiceTest extends BaseTest {

    @Autowired
    private OfflineOrderDetailCancelTaskService offlineOrderDetailCancelTaskService;

    @Test
    public void doTest() {
        offlineOrderDetailCancelTaskService.doCancelOrderDetail();
    }

}
