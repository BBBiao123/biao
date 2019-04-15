package com.biao.enums;

public enum OfflineAppealEnum {
    ING("1", "申诉中"),
    DONE("2", "处理完成"),
    CANCEL("3", "撤销"),;
    private String code;

    private String message;

    OfflineAppealEnum(String code, String message) {
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
