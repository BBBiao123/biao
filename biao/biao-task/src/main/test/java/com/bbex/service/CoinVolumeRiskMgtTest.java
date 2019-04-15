package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""zj
 */
public class CoinVolumeRiskMgtTest extends BaseTest {

    @Autowired
    private CoinVolumeRiskMgtTaskService coinVolumeRiskMgtTaskService;

    @Test
    public void triggerCoinVolumeRiskMgtEntry() {
        coinVolumeRiskMgtTaskService.triggerCoinVolumeRiskMgtEntry();
    }
}