package com.biao.enums;

public enum DepositCoinTypeEnum {

    BTC("0", "BTC"),
    ETH("1", "ETH"),
    EOS("2", "EOS"),
    CKT("3", "CKT");
    private String code;

    private String message;

    DepositCoinTypeEnum(String code, String message) {
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
