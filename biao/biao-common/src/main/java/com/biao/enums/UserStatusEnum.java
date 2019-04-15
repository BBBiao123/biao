package com.biao.enums;

public enum UserStatusEnum {

    USER_NORMAl("0", "用户正常"),
    USER_LOCK("9", "用户锁定"),
    USER_DISABLE("8", "用户禁用");
    private String code;

    private String message;

    UserStatusEnum(String code, String message) {
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
