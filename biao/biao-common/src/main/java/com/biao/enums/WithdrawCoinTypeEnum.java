package com.biao.enums;

public enum WithdrawCoinTypeEnum {

    BTC("0", "BTC"),
    ETH("0", "ETH"),
    QTUM("0", "QTUM"),
    NEO("0", "NEO"),
    EOS("0", "EOS"),
    LTC("0", "LTC"),
    ERC20("1", "ERC20"),
    USDT("5", "USDT");
    private String code;

    private String message;

    WithdrawCoinTypeEnum(String code, String message) {
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
