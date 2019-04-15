package com.biao.service.impl;


import com.biao.BaseTest;
import com.biao.service.OfflineCoinVolumeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 *  ""(""611 @ qq.com)
 */
public class OfflineCoinVolumeServiceImplTest extends BaseTest {

    @Autowired
    OfflineCoinVolumeService offlineCoinVolumeService;


    @Test
    public void in() {
        offlineCoinVolumeService.in("227187581610233856", "1", new BigDecimal(2), "usdt");
    }

    @Test
    public void out() {
    }
}
