package com.biao.enums;

public enum UserTagEnum {

    FINANCE("FM", "财务"),
    OTC_AGENT("YS", "银商"),
    OTC_BUSINESS("OTC-SH", "OTC商户"),
    OTC_ADVERT("OTC-GGS", "OTC广告商"),;
    private String code;

    private String message;

    UserTagEnum(String code, String message) {
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
