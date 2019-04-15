package com.biao.constant;

public enum TimeUnitEnum {

    HOUR("10", "时"),
    MINUTE("20", "分"),
    SECOND("30", "秒");

    private String code;
    private String message;

    TimeUnitEnum(String code, String message) {
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
