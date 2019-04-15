package com.biao.reactive.data.mongo.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RedisMatchStream {

    /**
     * 时间.
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime tradeTime;

    /**
     * 保存 分钟时间.
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
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
     * 主区交易量.
     */
    private BigDecimal totalVolume;

}
