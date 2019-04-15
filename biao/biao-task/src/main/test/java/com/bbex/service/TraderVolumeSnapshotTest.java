package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""(Myth)
 */
public class TraderVolumeSnapshotTest extends BaseTest {

    @Autowired
    private TraderVolumeSnapshotTaskService traderVolumeSnapshotTaskService;

    @Test
    public void triggerTraderVolumeSnapshotEntry() {
        traderVolumeSnapshotTaskService.triggerTraderVolumeSnapshotEntry();
    }
}