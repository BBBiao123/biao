package com.biao.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * <p>Description: .</p>
 *
 *  ""(Myth)
 * @version 1.0
 * @date 2018/5/5 16:50
 * @since JDK 1.8
 */
@Getter
@RequiredArgsConstructor
public enum ExTypeEnum {


    BUY(0, "买入"),

    SELL(1, "卖出");

    private final int code;

    private final String msg;

    public static ExTypeEnum getByCode(int code) {
        return Arrays.stream(ExTypeEnum.values())
                .filter(exTypeEnum -> exTypeEnum.getCode() == code).findFirst().orElse(ExTypeEnum.BUY);
    }

    public static String buildMsgByCode(int code) {
        return getByCode(code).getMsg();
    }


}
