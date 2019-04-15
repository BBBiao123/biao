package com.biao.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 推送平台交易页面数据VO.
 *
 *  ""
 * @version 1.0
 * @date 2018/4/6 上午11:18
 * @since JDK 1.8
 */
@Getter
@Setter
@ToString
public class PlatTradePushVO implements Serializable {

    private BigDecimal btcLastPrice;

    private BigDecimal ethLastPrice;

    private BigDecimal eosLastPrice;

    private BigDecimal cnbBtcLastPrice;

    private BigDecimal cnbEthLastPrice;

    private BigDecimal cnbEosLastPrice;


    /**
     * 撮合流水集合.
     */
    private MatchStreamVO matchStreamVO;


}
