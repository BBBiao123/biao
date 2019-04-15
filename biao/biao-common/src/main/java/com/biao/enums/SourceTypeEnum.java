package com.biao.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * The enum Source type enum.
 *
 *  ""(Myth)
 */
@Getter
@RequiredArgsConstructor
public enum SourceTypeEnum {


    /**
     * Web source type enum.
     */
    WEB("web"),

    /**
     * Android source type enum.
     */
    ANDROID("android"),

    /**
     * Ios source type enum.
     */
    IOS("ios");


    private final String msg;

    /**
     * Gets by msg.
     *
     * @param msg the msg
     * @return the by msg
     */
    public static SourceTypeEnum getByMsg(String msg) {
        return Arrays.stream(SourceTypeEnum.values())
                .filter(typeEnum -> typeEnum.getMsg().equals(msg)).findFirst().orElse(null);
    }

}
