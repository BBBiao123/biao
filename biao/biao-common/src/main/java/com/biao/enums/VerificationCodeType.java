package com.biao.enums;

import java.util.Arrays;

public enum VerificationCodeType {

	LOGIN_CODE("login", "登录验证码"), REGISTER_CODE("register", "注册验证码"),
    EX_TRADE_PASS("ex_password", "交易密码设置"),
    EX_TRADE_MOBILE("ex_mobile", "交易手机短信验证"),
    EX_TRADE_MAIL("ex_mail", "交易邮箱邮件验证"),
    EX_EXCHANGE_PASS("exchange_pass", "邮箱验证切换交易方式"),
    BINDER_GOOGLE_CODE("bander_google", "绑定谷歌验证"),
    LOGIN_CHANGE_CODE("login_change", "登录变更邮件码"),
    WITHDRAW_CODE("withdraw", "提现码"),
    MESSAGE_CODE("message", "短信验证码"),
    RESET_CODE("reset", "重置密码"),
    BINDER_CODE("binder", "绑定邮箱"),
    LOGIN_MAIL_CODE("mail_login", "邮件登录验证码"),
    BINDER_GOOGLE_CODE_MAIL("bander_google_mail", "邮件绑定谷歌验证"),
    EX_PASS_MAIL("ex_password_mail", "邮箱设置密码交易方式");
    private String code;

    private String message;

    VerificationCodeType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static VerificationCodeType valueToEnums(String value) {
        return Arrays.stream(VerificationCodeType.values()).filter(enums -> enums.getCode().equals(value)).findFirst().orElse(MESSAGE_CODE);
    }

}