package com.biao.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *  ""
 */

@Getter
@RequiredArgsConstructor
public enum OfflineFeeTypeEnum {

    NONE("0", "不收取"),
    STATIC("1", "固定"),
    SCALE("2", "比例");
    private final String code;

    private final String message;

}
