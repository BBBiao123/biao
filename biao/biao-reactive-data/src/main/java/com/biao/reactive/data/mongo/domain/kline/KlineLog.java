package com.biao.reactive.data.mongo.domain.kline;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Kline 流水日志. 自家交易所，其他交易所拉取的时候，都往这里面存储
 *
 *  ""(Myth)
 */
@Document
@Data
public class KlineLog {

    @Id
    private String id;

    /**
     * 创建时间.
     */
    private LocalDateTime createTime;

    /**
     * 交易时间.
     */
    private LocalDateTime tradeTime;

    /**
     * 交易分钟时间.
     */
    private LocalDateTime minuteTime;

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

}
