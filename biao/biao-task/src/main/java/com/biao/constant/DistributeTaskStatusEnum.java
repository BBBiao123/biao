package com.biao.constant;

public enum DistributeTaskStatusEnum {

    SUCCESS("1", "成功"),
    FAILURE("0", "失败");
    private String code;

    private String message;

    DistributeTaskStatusEnum(String code, String message) {
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
