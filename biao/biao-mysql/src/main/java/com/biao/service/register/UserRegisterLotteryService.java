package com.biao.service.register;

import com.biao.lottery.LotteryVO;

/**
 * The interface User register lottery service.
 *
 *  ""(Myth)
 */
public interface UserRegisterLotteryService {

    /**
     * Check lottery change boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    Boolean checkLottery(String userId);


    /**
     * Lottery string.
     *
     * @param userId the user id
     * @param source the source
     * @return the LotteryVO
     */
    LotteryVO lottery(String userId, String source);


    /**
     * Execute lottery refer.
     */
    void executeLotteryRefer();
}
