package com.biao.enums;

public enum DepositStatusEnum {

    INIT("0", "确认中"),
    CANCEL("1", "已成功");
    private String code;

    private String message;

    DepositStatusEnum(String code, String message) {
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
