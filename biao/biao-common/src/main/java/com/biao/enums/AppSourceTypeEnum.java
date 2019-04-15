package com.biao.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppSourceTypeEnum {

    OTC("otc", "otc"),
    PLAT("plat", "plat");
    private final String code;

    private final String message;
}
