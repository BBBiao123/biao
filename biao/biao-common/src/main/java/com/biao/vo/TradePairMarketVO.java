package com.biao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 交易对数据结构.
 *
 *  ""
 * @version 1.0
 * @date 2018/4/6 上午10:31
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradePairMarketVO extends TradePairBaseVO {

    /**
     * 最新价.
     */
    private BigDecimal latestPrice;

    /**
     * 涨幅.
     */
    private String rise;


    /**
     * 当天初始的价格.
     */
    private BigDecimal firstPrice;

}
