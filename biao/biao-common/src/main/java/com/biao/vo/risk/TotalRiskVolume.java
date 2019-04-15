package com.biao.vo.risk;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  ""(Myth)
 */
@Data
public class TotalRiskVolume {

    private String coinSymbol;

    /**
     * 转币
     */
    private BigDecimal historyVolume;

    /**
     * 冲币
     */
    private BigDecimal depositVolume;

    /**
     * 提币
     */
    private BigDecimal withdrawVolume;

    /**
     * 总量
     */
    private BigDecimal totalVolume;
}
