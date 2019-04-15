package com.biao.enums;

public enum ExPairStatusEnum {

    OFFLINE("0", "未发布"),
    ONLINE("1", "已发布");
    private String code;

    private String message;

    ExPairStatusEnum(String code, String message) {
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
