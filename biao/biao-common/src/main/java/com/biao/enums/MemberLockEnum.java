package com.biao.enums;

/**
 * 会员冻结类型枚举
 */
public enum  MemberLockEnum {

    RECHARGE_LOCK("1", "会员冻结-充值冻结"),
    ADVERTISER_LOCK("2", "会员冻结-广告商冻结"),
    REWARD_LOCK("3", "会员冻结-活动奖励冻结"),
    ;
    private String code;
    private String mark;
    MemberLockEnum(String code, String mark) {
        this.code = code;
        this.mark = mark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
