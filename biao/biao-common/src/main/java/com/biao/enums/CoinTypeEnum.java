package com.biao.enums;

public enum CoinTypeEnum {

    NO("0", "NO"),
    ETH("1", "ETH"),
    QTUM("2", "QTUM"),
    NEO("3", "NEO"),
    EOS("4", "EOS"),
    BTC("5", "BTC");
    private String code;

    private String message;

    CoinTypeEnum(String code, String message) {
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
