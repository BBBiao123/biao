package com.biao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户c2c资产vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfflineVolumeVO implements Serializable {

    private String coinId;
    private String symbol;
    private BigDecimal volume;
    private BigDecimal advertVolume;
    private BigDecimal lockVolume;
    private BigDecimal otcAdvertVolume;
    private BigDecimal otcLockVolume;
    private BigDecimal bailVolume;
    private String isChangeAccount; //是否开启转账
    private BigDecimal realDayLimit; //实名转账限额
    private BigDecimal nonRealDayLimit; //非实名转账限额
    private Integer pointVolume; //小数位
    private BigDecimal changeMinVolume; //转账最低数量


}
