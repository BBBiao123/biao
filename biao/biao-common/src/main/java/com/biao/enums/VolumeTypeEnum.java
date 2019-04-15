package com.biao.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *  ""
 */

@Getter
@RequiredArgsConstructor
public enum VolumeTypeEnum {

    COIN_VOLUME("0", "币币资产"),
    OFFLINE_VOLUME("1", "c2c资产"),
    OFFLINE_BAIL_VOLUME("2", "保证金");
    private final String code;

    private final String message;

}
