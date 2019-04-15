package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 提币地址管理
 */
@Setter
@Getter
public class WithdrawAddressVO {
    private String coinId;
    private String symbol;
    private String address;
    private String tag;
}
