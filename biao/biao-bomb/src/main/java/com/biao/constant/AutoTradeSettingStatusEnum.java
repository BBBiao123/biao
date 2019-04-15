package com.biao.constant;

public enum AutoTradeSettingStatusEnum {

    FORBIDDEN("0", "禁用"),
    STARTUP("1", "启动");

    private String code;
    private String message;

    AutoTradeSettingStatusEnum(String code, String message) {
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
