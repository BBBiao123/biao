package com.biao.enums;

public enum WithdrawAddressStatusEnum {

    INIT("0", "正常"),
    CANCEL("9", "已删除");
    private String code;

    private String message;

    WithdrawAddressStatusEnum(String code, String message) {
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
