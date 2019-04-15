package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RegisterUserCoinServiceTest extends BaseTest {

    @Autowired
    RegisterUserCoinService registerUserCoinService;

    @Test
    public void test() {
        registerUserCoinService.registerGiveCoin();
    }
}
