package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RegisterUserCoinRepairServiceTest extends BaseTest {

    @Autowired
    RegisterUserCoinRepairService registerUserCoinRepairService;

    @Test
    public void test() {
        registerUserCoinRepairService.doTimeRepair();
    }
}
