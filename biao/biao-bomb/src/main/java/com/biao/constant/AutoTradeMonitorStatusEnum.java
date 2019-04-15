package com.biao.constant;

public enum AutoTradeMonitorStatusEnum {

    READY("0", "就绪"),
    RUNNING("1", "运行中"),
    END("4", "结束");

    private String code;
    private String message;

    AutoTradeMonitorStatusEnum(String code, String message) {
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
