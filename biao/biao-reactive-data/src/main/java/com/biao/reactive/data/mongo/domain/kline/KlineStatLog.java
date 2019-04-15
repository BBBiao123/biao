package com.biao.reactive.data.mongo.domain.kline;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Kline 统计日志 存每个交易对 小时以上单位的统计数据,包括 1 2 4 6 8 12 一天 一周 一个月
 *
 *  ""(Myth)
 */
@Document
@Data
public class KlineStatLog {

    @Id
    private String id;

    /**
     * Kline 统计时间单位 {@linkplain com.biao.enums.KlineTimeEnum}
     */
    private String klineTimeUnit;

    /**
     * 交易时间.
     */
    private LocalDateTime tradeTime;

    /**
     * 主区 币代号  eth_btc 这里就是btc.
     */
    private String coinMain;

    /**
     * 其他币种代号 eth_btc 这里存的就是eth.
     */
    private String coinOther;

    /**
     * 时间.
     */
    private String t;

    /**
     * 开盘价.
     */
    private String o;

    /**
     * 最高价.
     */
    private String h;

    /**
     * 最低价.
     */
    private String l;

    /**
     * 收盘价.
     */
    private String c;

    /**
     * 成交量.
     */
    private String v;

}
