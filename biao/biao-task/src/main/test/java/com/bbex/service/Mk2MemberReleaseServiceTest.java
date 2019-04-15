package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Mk2MemberReleaseServiceTest extends BaseTest {

    @Autowired
    private Mk2MemberReleaseService mk2MemberReleaseService;

    @Test
    public void testRelease() {
        mk2MemberReleaseService.releaseLockVolume();
    }

}
