package com.thinkgem.jeesite.modules.plat.enums;

public enum MessageTemplateCode {

    REGISTER_TEMPLATE("register", "注册模板"),
    WITHDRAW_TEMPLATE("withdraw", "提现邮件模板"),
    MESSAGE_TEMPLATE("message", "短信模板"),
    RESET_TEMPLATE("reset", "邮件重置密码模板"),
    LOGIN_TEMPLATE("login", "邮件登录模板"),
    EMAIL_BINDER_TEMPLATE("binder_email", "手机绑定邮箱"),
    EMAIL_EX_TRADE_TEMPLATE("email_ex_pass", "邮件交易密码验证"),
    EMAIL_EXCHANGE_PASS_TEMPLATE("email_exchange_pass", "邮件切换交易方式验证"),
    MOBILE_REGISTER_TEMPLATE("register_mobile", "手机注册模板"),
    MOBILE_TRADE_PASSWORD_TEMPLATE("trade_pass_mobile", "手机交易密码设置模板"),
    MOBILE_BINDER_GOOGLE_TEMPLATE("binder_google", "手机绑定谷歌验证模板"),
    MOBILE_BINDER_TEMPLATE("binder_mobile", "手机绑定模板"),
    MOBILE_LOGIN_TEMPLATE("login_mobile", "手机登录模板"),
    MOBILE_RESET_TEMPLATE("reset_pwd", "手机找回密码模板"),
    MOBILE_SYN_TEMPLATE("syn_pwd", "用户同步密码短信发送"),
    MOBILE_BINDER_UPDATE("update_mobile", "修改手机号"),
    MOBILE_EX_TRADE_TEMPLATE("mobile_ex_pass", "短信交易密码验证"),
    MOBILE_EXCHANGE_PASS_TEMPLATE("mobile_exchange_pass", "短信切换交易方式验证"),
    MOBILE_APPEAL_TEMPLATE("appeal_detail", "申诉通知对方"),
    MOBILE_LOGIN_ERROR_TIME_TEMPLATE("login_error_times", "登录错误超次数提示"),
    LOCK_PLAT_USER("lock_plat_user", "会员锁定短信发送"),
    UN_LOCK_PLAT_USER("un_lock_plat_user", "会员解锁短信发送"),
    ;
    private String code;

    private String message;

    MessageTemplateCode(String code, String message) {
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
