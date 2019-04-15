package com.biao.constant;

public enum DistributeRuleTypeEnum {

    MINING("1", "挖矿规则"),
    DIVIDEND("2", "分红规则"),
    PROMOTE("3", "会员推广"),
    EXFEE_REMIT("4", "手续费打款");
    private String code;

    private String message;

    DistributeRuleTypeEnum(String code, String message) {
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
