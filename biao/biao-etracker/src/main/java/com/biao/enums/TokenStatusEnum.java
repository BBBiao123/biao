package com.biao.enums;

/**
 * 充值提现状态  9：不可以充值提现 1 ：可以充值 可以提现 2：仅可以充值 3：仅可以提现
 */
public enum TokenStatusEnum {

    IN_OUT("1", "可以充值提现"),
    ONLY_IN("2", "仅可以充值"),
    ONLY_OUT("3", "仅可以提现"),
    NOT_IN_OUT("9", "不可以充值提现");
    private String code;

    private String message;

    TokenStatusEnum(String code, String message) {
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
