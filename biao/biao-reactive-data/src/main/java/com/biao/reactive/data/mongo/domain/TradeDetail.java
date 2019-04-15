package com.biao.reactive.data.mongo.domain;

import com.biao.enums.OrderEnum;
import com.biao.enums.TradeEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 */
@Data
@Document
public class TradeDetail {

    private @Id
    String id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 交易号；
     */
    private String tradeNo;
    /**
     * 时间
     */
    private LocalDateTime tradeTime;

    /**
     * 保存 分钟时间
     */
    private LocalDateTime minuteTime;
    /**
     * 手续费用；
     */
    private BigDecimal exFee;

    /**
     * 买入 / 卖出
     */
    private Integer type;

    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 最后价格
     */
    private BigDecimal lastPrice;

    /**
     * 成交数量
     */
    private BigDecimal volume;

    /**
     * 主区 币代号  eth_btc 这里就是btc
     */
    private String coinMain;

    /**
     * 其他币种代号 eth_btc 这里存的就是eth
     */
    private String coinOther;
    /**
     * 当前交易userId;
     */
    private String userId;
    /**
     * 交易对用户Id;
     */
    private String toUserId;
    /**
     * 当前是由谁主动触发交易；
     */
    private TradeEnum mainTrade;
    /**
     * 交易的volume;
     */
    private BigDecimal tradeVolume;
    /**
     * 目前订单的状态是什么呢？
     */
    private OrderEnum.OrderStatus status;
}
