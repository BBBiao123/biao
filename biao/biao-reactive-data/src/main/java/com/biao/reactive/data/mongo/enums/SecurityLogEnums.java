package com.biao.reactive.data.mongo.enums;

public enum SecurityLogEnums {

    //1:修改密码   3:重置密码  2:绑定手机号  4:修改手机号  5:绑定谷歌  6:绑定交易密码  7:修改交易密码   8:切换交易类型  9:设置交易类型   ;
    SECURITY_UPDATE_PASSWORD("1", "修改密码 "),
    SECURITY_RESET_PASSWORD("3", "重置密码"),
    SECURITY_BINDER_MOBILE("2", "绑定手机号"),
    SECURITY_UPDATE_MOBILE("4", "修改手机号"),
    SECURITY_BINDER_GOOGLE("5", "绑定谷歌"),
    SECURITY_BINDER_EX_PASS("6", "绑定交易密码"),
    SECURITY_UPDATE_EX_PASS("7", "修改交易密码"),
    SECURITY_UPDATE_EX_TYPE("8", "切换交易类型 "),
    SECURITY_SET_EX_TYPE("9", "设置交易类型");
    private String code;

    private String message;

    SecurityLogEnums(String code, String message) {
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
