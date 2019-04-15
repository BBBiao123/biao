package com.thinkgem.jeesite.modules.plat.enums;

public enum PlatUserOplogTypeEnum {

    AUDIT("1","审核"),
    LOCK("2", "锁定"),

    C2C_IN("4","转入C2C"),
    C2C_OUT("6","转出C2C"),
    PUBLISH("8","发布广告"),
    COIN_OUT("10","提现"),
    CHANGE("12","禁用转账"),
    C2C_SWITCH("14","C2C交易"),

    CHANGE_MOBILE("16", "修改手机"),
    CHANGE_TAG("17", "修改标识"),
    CLEAR_TRADE_CACHE("18", "清空资产缓存"),

    CLEAR_MAIL("19", "清空邮件"),
    CLEAR_GOOGLE("20", "清空谷歌"),
    CLEAR_PASS("21", "清空登录次数"),
    CLEAR_EX_PASS("22", "清空交易次数"),
    CLEAR_AD_CANCEL_LOG("23", "清空发广告次数"),
    TRADE_SWITCH("24","币币交易"),
    LOCK_DATE("25","资产锁定"),
    ;

    PlatUserOplogTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code ;

    private String message ;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
