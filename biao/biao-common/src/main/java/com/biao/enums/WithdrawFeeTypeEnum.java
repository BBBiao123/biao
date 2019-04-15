package com.biao.enums;

public enum WithdrawFeeTypeEnum {

    RATE("0", "按比例"),
    NUMBER("1", "按固定数量");
    private String code;

    private String message;

    WithdrawFeeTypeEnum(String code, String message) {
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
