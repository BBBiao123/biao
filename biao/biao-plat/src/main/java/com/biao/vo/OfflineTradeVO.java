package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
public class OfflineTradeVO implements Serializable {

    private String coinId;
    private String symbol;
    private BigDecimal exType;
    private BigDecimal volume;
    private BigDecimal price;
    private String orderId;
    private String mobile;
    /**
     * 交易密码
     */
    private String exPassword;


}
