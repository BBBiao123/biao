package com.biao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * offline转账
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfflineChangeVO implements Serializable {

    private String changeNo;
    private String coinId;
    private String symbol;
    private BigDecimal volume;
    private BigDecimal myVolumeLimit; //
    private BigDecimal fee;
    private String toAccount;
    private String realName;
    private Integer pointVolume;
    private BigDecimal changeMinVolume;


}
