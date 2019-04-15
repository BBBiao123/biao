package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * C2C广告
 */
@Getter
@Setter
public class OfflineAdvertVO implements Serializable {


    private String coinId;
    private BigDecimal volume;
    private BigDecimal minExVolume;
    private String symbol;
    private Integer exType;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private String remarks;

    private String exPassword;


}
