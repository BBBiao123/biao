package com.biao.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/5/20 下午6:45
 * @since JDK 1.8
 */
@Data
public class LastPriceVO implements Serializable {

    private BigDecimal btcLastPrice;

    private BigDecimal ethLastPrice;

    private BigDecimal eosLastPrice;

    private BigDecimal cnbBtcLastPrice;

    private BigDecimal cnbEthLastPrice;

    private BigDecimal cnbEosLastPrice;

}
