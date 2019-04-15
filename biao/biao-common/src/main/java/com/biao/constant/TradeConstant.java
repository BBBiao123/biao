package com.biao.constant;

import java.math.BigDecimal;

/**
 *
 * @date 2018/4/15
 * 关于交易类的常量
 * 交易失入的错误代码从：9开头
 */
public class TradeConstant implements Constants {

    public static final BigDecimal TRADE_LENGTH = new BigDecimal(100000000);
    /**
     * 交易失败
     */
    public static final Integer TRAND_FAILURE = 900001;
    /**
     * 主要的交易信息kafka topic
     */
    public static final String TRADE_KAFKA_TOPIC = "tradeTopic";

    /**
     * 接收kafka消息 用户手续费处理的topic.
     */
    public static final String TRADE_COIN_FEE_KAFKA_TOPIC = "tradeCoinFeeKafkaTopic";
    /**
     * 发送一个消息通知挂单情况；
     */
    public static final String TRADE_KAFKA_ORDER_TOPIC = "tradeKafkaOrderTopic";
    /**
     * 订单信息异步落库;
     */
    public static final String TRADE_KAFKA_ORDER_SAVE_TOPIC = "tradeKafkaOrderSaveTopic";
    /**
     * 发送挂单数据的处理;
     */
    public static final String TRADE_KAFKA_ORDER_BUYANDSELL_TOPIC = "tradeKafkaOrderBuyAndSellTopic";
    /**
     * 推送结果给前端消息；
     */
    public static final String TRADE_RESULT_MATCH_TOPIC = "tradeResultMatchTopic";

    public static final String C2C_USER_ORDER = "C2cUserOrder";

    public static final String MESSAGE = "message";

    public static final String KLINE_MIN_TRANSFER = "klineMinTransfer";


    /**
     * 推送结果给前端消息；
     */
    public static final String TRADE_RESULT_ORDER_TOPIC = "tradeResultOrderTopic";
    /**
     * 用于前置处理判断
     */
    public static final String TRADE_PREPOSITION_KEY = "trade:redis:pre";
    /**
     * redis买入的redis key
     */
    public static final String TRADE_REDIS_BUY_SET = "trade:redis:buy:set";
    /**
     * redis买出的 reids key
     */
    public static final String TRADE_REDIS_SELL_SET = "trade:redis:sell:set";
    /**
     * 判断是否已经进入交易引擎
     */
    public static final String TRADE_REDIS_ENGINE_ID_KEY = "trade:redis:engine_id";

    /**
     * 首页socket key标识
     */
    public static final String HOME_EXPAIR = "home_expair";
}
