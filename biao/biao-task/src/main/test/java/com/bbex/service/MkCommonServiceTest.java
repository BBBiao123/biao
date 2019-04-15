package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MkCommonServiceTest extends BaseTest {

    @Autowired
    private MkCommonService mkCommonService;

    @Test
    public void testCommon() {
        mkCommonService.mk2StatisticsCoin2User();
    }
}
