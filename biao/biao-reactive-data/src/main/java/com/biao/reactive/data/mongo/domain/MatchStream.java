package com.biao.reactive.data.mongo.domain;

import com.biao.enums.OrderEnum;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 撮合流水实体.
 *
 *  ""
 */
@Data
@RequiredArgsConstructor
@Document
public class MatchStream {

    @Id
    private String id;

    /**
     * 时间.
     */
    private LocalDateTime tradeTime;

    /**
     * 保存 分钟时间.
     */
    private LocalDateTime minuteTime;

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
     * 主区 币代号  eth_btc 这里就是btc.
     */
    private String coinMain;

    /**
     * 其他币种代号 eth_btc 这里存的就是eth.
     */
    private String coinOther;

    /**
     * 挂单号.
     */
    private String orderNo;

    /**
     * 主区交易量.
     */
    private BigDecimal totalVolume;

    /**
     * 交易的交易号.
     */
    private String tradeNo;
    /**
     * 交易的用户ID;
     */
    private String userId;
    /**
     * 被交易者的用户ID;
     */
    private String toUserId;
    /**
     * 当前用于的状态；
     */
    private OrderEnum.OrderStatus status;
}
