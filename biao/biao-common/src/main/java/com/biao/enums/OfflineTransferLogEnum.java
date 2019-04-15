package com.biao.enums;

public enum OfflineTransferLogEnum {

    COIN_TO_OFFLINE("0", "常规账户转入到c2c"),
    OFFLINE_TO_COIN("1", "c2c转出到常规"),
    OFFLINE_TO_BAIL("3", "c2c转入保证金"),
    BAIL_TO_OFFLINE("4", "保证金转入到c2c"),
    COIN_TO_BAIL("5", "常规转入保证金"),
    BAIL_TO_COIN("6", "保证金转入到常规"),
    REGISTER_LOTTERY("7", "注册抽奖"),
    REGISTER_LOTTERY_RECOMMEND("8", "注册抽奖-推荐人"),
    COIN_TO_SUPER("11", "常规账户转入到超级钱包"),
    SUPER_TO_COIN("12", "超级钱包转出到常规账户"),
    ;
    private String code;

    private String message;

    OfflineTransferLogEnum(String code, String message) {
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
