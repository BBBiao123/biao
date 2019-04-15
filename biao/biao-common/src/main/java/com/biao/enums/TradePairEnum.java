package com.biao.enums;

import com.biao.constant.Constants;
import com.biao.constant.RedisConstants;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * project :biao
 * 交易 主区 枚举定义
 *
 *  ""
 * @version 1.0
 * @date 2018/4/15 下午2:15
 * @since JDK 1.8
 */
@Getter
public enum TradePairEnum {


    USDT(1, "USDT"),

    BTC(2, "BTC"),

    ETH(3, "ETH"),

    CNB(4, "CNB"),

    USDB(5, "USDB"),

    EUC(6, "EUC"),

    VRT(7, "VRT"),

    EOS(8, "EOS");

    private int sort;

    private String key;


    TradePairEnum(int sort, String key) {
        this.sort = sort;
        this.key = key;
    }

    public static List<String> buildKey() {
        return Arrays.stream(TradePairEnum.values())
                .map(m -> RedisConstants.TASK_TRADE + m.getKey())
                .collect(Collectors.toList());

    }

    public static List<String> buildKeys() {
        return Arrays.stream(TradePairEnum.values())
                .map(TradePairEnum::getKey)
                .collect(Collectors.toList());

    }


    public static TradePairEnum valueToEnums(String valueKey) {
        return Arrays.stream(TradePairEnum.values()).filter(enums -> enums.getKey().equalsIgnoreCase(valueKey)).findFirst().orElse(USDT);
    }

    public static String buildTradeKey(String valueKey) {
        TradePairEnum pairEnum = valueToEnums(valueKey);
        return RedisConstants.TASK_TRADE + pairEnum.getKey();
    }

    public static String buildIncrementTradeKey(String coinMain, String coinOther) {
        TradePairEnum coinMainEnum = valueToEnums(coinMain);
        return RedisConstants.TASK_TRADE_INC + coinMainEnum.getKey() + Constants.JOIN + coinOther;
    }

    public static String buildMinForDayTradeKey(String valueKey) {
        TradePairEnum pairEnum = valueToEnums(valueKey);
        return "task:trade-date:" + pairEnum.getKey();
    }

}
