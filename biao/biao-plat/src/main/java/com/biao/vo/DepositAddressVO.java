package com.biao.vo;

import lombok.Data;

/**
 * 币种充值
 * dazi
 */
@Data
public class DepositAddressVO {
    private String tag;
    private String address;

    public DepositAddressVO(String address, String tag) {
        this.tag = tag;
        this.address = address;
    }
}
