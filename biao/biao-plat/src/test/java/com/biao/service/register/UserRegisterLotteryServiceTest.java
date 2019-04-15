package com.biao.service.register;

import com.biao.BaseTest;
import com.biao.lottery.LotteryVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""(Myth)
 */
public class UserRegisterLotteryServiceTest extends BaseTest {

    @Autowired
    private UserRegisterLotteryService userRegisterLotteryService;

    @Test
    public void checkLottery() {
        final Boolean aBoolean = userRegisterLotteryService.checkLottery("252200802112901123");
    }

    @Test
    public void lottery() {
        final LotteryVO lotteryVO = userRegisterLotteryService.lottery("888800802112901120", "ios");
        System.out.println(lotteryVO);
    }

    @Test
    public void executeLotteryRefer() {
        userRegisterLotteryService.executeLotteryRefer();
    }
}