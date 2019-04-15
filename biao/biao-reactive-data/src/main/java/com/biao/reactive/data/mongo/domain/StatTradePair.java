package com.biao.reactive.data.mongo.domain;

import com.biao.vo.TradePairVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/21 下午5:28
 * @since JDK 1.8
 */
@Data
@Document
public class StatTradePair {

    private @Id
    String id;

    /**
     * 最高价
     */
    private BigDecimal highestPrice;

    /**
     * 最低价
     */
    private BigDecimal lowerPrice;

    /**
     * 最新价
     */
    private BigDecimal latestPrice;

    /**
     * 当天初始的价格
     */
    private BigDecimal firstPrice;

    /**
     * 一天的量
     */
    private BigDecimal dayCount;

    /**
     * 🐽主区 币代号  eth_btc 这里就是btc
     */
    private String coinMain;

    /**
     * 其他币种代号 eth_btc 这里存的就是eth
     */
    private String coinOther;

    /**
     * 哪一天
     */
    private LocalDate today;

    /**
     * 具体分钟
     */
    private LocalDateTime minuteTime;

    public static StatTradePair convertFromVo(TradePairVO tradePairVO, LocalDate date) {
        StatTradePair statTradePair = new StatTradePair();
        BeanUtils.copyProperties(tradePairVO, statTradePair);
        statTradePair.setToday(date);
        return statTradePair;
    }
}
