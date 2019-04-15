package com.biao.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 币种充值
 * dazi
 */
@Data
public class DepositVO {
    private String coinId;
    private String coinSymbol;
    private String address;
    private BigDecimal volume;
}
