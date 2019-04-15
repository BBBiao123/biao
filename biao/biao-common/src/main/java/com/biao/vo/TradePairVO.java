package com.biao.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易对数据结构.
 *
 *  ""
 */
@Getter
@Setter
@ToString
@Data
public class TradePairVO extends TradePairMarketVO {

    /**
     * 最高价.
     */
    private BigDecimal highestPrice;

    /**
     * 最低价.
     */
    private BigDecimal lowerPrice;

    /**
     * 一天的量.
     */
    private BigDecimal dayCount;

    /**
     * 分钟时间.
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime minuteTime;


    private Integer pricePrecision;

    private Integer volumePrecision;

    private Integer volumePercent;

    private Integer type;

    @Override
    public String toString() {
        return "TradePairVO{" +
                "highestPrice=" + highestPrice +
                ", lowerPrice=" + lowerPrice +
                ", dayCount=" + dayCount +
                ", minuteTime=" + minuteTime +
                '}';
    }
}