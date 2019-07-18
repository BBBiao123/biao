package com.bbex.enums;

public enum WithdrawStatusEnum {

    INIT("0", "未处理"),
    AUDIT_PASS("1", "审核通过"),
    AUDIT_NOT_PASS("2", "审核不通过"),
    PAY("3", "已汇出"),
    CANCEL("9", "已取消");
    private String code;

    private String message;

    WithdrawStatusEnum(String code, String message) {
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
