package com.biao.coin;

import com.biao.constant.Constants;
import com.biao.constant.RedisConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Coin main service.
 *
 *  ""(Myth)
 */
@Getter
@Setter
public class CoinMainService {

    private List<String> list = new ArrayList<>();

    /**
     * Build trade key string.
     *
     * @param coinMain the coin main
     * @return the string
     */
    public String buildTradeKey(final String coinMain) {
        return RedisConstants.TASK_TRADE + coinMain;
    }

    /**
     * Build increment trade key string.
     *
     * @param coinMain  the coin main
     * @param coinOther the coin other
     * @return the string
     */
    public String buildIncrementTradeKey(final String coinMain, final String coinOther) {
        return RedisConstants.TASK_TRADE_INC + coinMain + Constants.JOIN + coinOther;
    }

}
