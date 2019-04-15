package com.biao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户资产实体vo.
 *
 *  ""(Myth)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCoinVolumeVO implements Serializable {

    private String userId;

    private BigDecimal volume;

    private BigDecimal lockVolume;

    private String coinSymbol;

}
