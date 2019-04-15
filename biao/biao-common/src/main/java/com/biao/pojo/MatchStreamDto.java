package com.biao.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * MatchStreamDto.
 *
 *  ""
 */
@Data
public class MatchStreamDto {

    private String tradeTime;

    /**
     * 保存 分钟时间.
     */
    private String minuteTime;

    /**
     * 买入 / 卖出.
     */
    private Integer type;

    /**
     * 价格.
     */
    private BigDecimal price;

    /**
     * 成交数量.
     */
    private BigDecimal volume;

    /**
     * 🐽主区 币代号  eth_btc 这里就是btc.
     */
    private String coinMain;

    /**
     * 其他币种代号 eth_btc 这里存的就是eth.
     */
    private String coinOther;

    /**
     * 订单号.
     */
    private String orderNo;

    /**
     * 交易号.
     */
    private String tradeNo;

    private String userId;

    private String toUserId;

    /**
     * 交易单
     */
    List<UserOrderDTO> userOrderDTOList;

}

