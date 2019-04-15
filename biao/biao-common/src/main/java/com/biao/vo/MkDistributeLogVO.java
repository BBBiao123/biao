package com.biao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户每一笔交易手续费
 *
 *  ""
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MkDistributeLogVO implements Serializable {

    private String id;

    private String userId;

    private BigDecimal exFee;

    private String coinMain;

    private String coinOther;

    private String orderNo;

    //挖矿平台币
    private String platCoin;

    //返还平台币的数量
    private BigDecimal refundVolume;
}
