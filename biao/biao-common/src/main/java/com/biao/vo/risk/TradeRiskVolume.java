package com.biao.vo.risk;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  ""(Myth)
 */
@Data
public class TradeRiskVolume {

    private String coinSymbol;

    private BigDecimal coinVolume;

    private BigDecimal offlineVolume;

    private BigDecimal totalVolume;
}
