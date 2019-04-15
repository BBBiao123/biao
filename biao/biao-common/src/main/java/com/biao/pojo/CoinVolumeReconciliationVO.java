package com.biao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 资产对账信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinVolumeReconciliationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String coinSymbol;
    private BigDecimal transferVolume;
    private BigDecimal depositVolume;
    private BigDecimal withdrawVolume;
    private BigDecimal tradeVolume;
    private BigDecimal offlineVolume;
    private BigDecimal lockVolume;
    private BigDecimal minerVolume;
    private BigDecimal presentVolume;
    private BigDecimal realVolume;
    private BigDecimal accountVolume;
    private BigDecimal balance;


}
