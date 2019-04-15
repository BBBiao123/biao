package com.biao.constant;

public enum AutoTradeOrderStatusEnum {

    FAIL("0", "失败"),
    SUCCESS("1", "成功");

    private String code;
    private String message;

    AutoTradeOrderStatusEnum(String code, String message) {
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
