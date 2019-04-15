package com.biao.constant;

public enum DistributeRuleStatusEnum {

    FORBIDDEN("0", "禁用"),
    ENABLE("1", "启用"),
    END("2", "终结");
    private String code;

    private String message;

    DistributeRuleStatusEnum(String code, String message) {
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
