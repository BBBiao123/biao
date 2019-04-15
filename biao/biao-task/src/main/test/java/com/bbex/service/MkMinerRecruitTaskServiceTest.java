package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""
 */
public class MkMinerRecruitTaskServiceTest extends BaseTest {

    @Autowired
    private MkMinerRecruitTaskService mkMinerRecruitTaskService;

    @Test
    public void testReachStandardTaskEntry() {
        mkMinerRecruitTaskService.reachStandardTaskEntry();
    }
}