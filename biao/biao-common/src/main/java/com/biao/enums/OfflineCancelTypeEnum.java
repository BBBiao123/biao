package com.biao.enums;

public enum OfflineCancelTypeEnum {

    ADVERT("0", "广告"),
    ORDER("1", "订单");
    private String code;

    private String message;

    OfflineCancelTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
