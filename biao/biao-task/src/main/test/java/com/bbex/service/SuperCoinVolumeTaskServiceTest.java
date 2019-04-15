package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""zj
 */
public class SuperCoinVolumeTaskServiceTest extends BaseTest {

    @Autowired
    private SuperCoinVolumeTaskService superCoinVolumeTaskService;

    @Test
    public void triggerTraderVolumeSnapshotEntry() {
        superCoinVolumeTaskService.triggerHandleExpireAccountEntry();
    }


}