package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 币种提现
 */
@Setter
@Getter
public class WithdrawVO {
    private String tag;
    private String userId;
    private String coinId;
    private String symbol;
    private String address;
    private BigDecimal volume;
}
