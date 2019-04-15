package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""(Myth)
 */
public class OfflineRedEnvelopeServiceTaskTest extends BaseTest {

    @Autowired
    private OfflineRedEnvelopeService offlineRedEnvelopeService;

    @Test
    public void triggerTraderVolumeSnapshotEntry() {
        offlineRedEnvelopeService.handleExpired();
    }
}