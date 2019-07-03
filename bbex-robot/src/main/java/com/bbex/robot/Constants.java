package com.bbex.robot;

/**
 * 全局变量表；
 */
public class Constants {
    /**
     * 设置一下buyUrl；
     */
    public static final String BUY_URL = "bbex/trade/buyIn";
    /**
     * sell url key;
     */
    public static final String SELL_URL = "bbex/trade/sellOut";
    /**
     * 获取订单号的接口；
     */
    public static final String ORDER_NO_URL = "bbex/trade/getOrderNo";
    /**
     * 登录的接口信息；
     */
    public static final String LOGIN = "bbex/user/login";
    /**
     * 主区key
     */
    public static final String COIN_MAIN = "coinMain";
    /**
     * 交易对币总key;
     */
    public static final String COIN_OTHER = "coinOther";
    /**
     * 交易的数量；
     */
    public static final String TRADE_VOLUME = "volume";
    /**
     * 交易的单价；
     */
    public static final String TRADE_PRICE = "price";
    /**
     * 交易的用户ID;
     */
    public static final String TRADE_USER_ID = "userId";
    /**
     * 订单号；
     */
    public static final String TRADE_ORDER_NO = "orderNo";
    /**
     * 获取数据；
     */
    public static final String STOKEN = "stoken";
    /**
     * 用户登录相关的处理；
     */
    public static final String LOGIN_USERNAME = "username";
    public static final String LOGIN_PASSWORD = "password";
    public static final String LOGIN_TOKEN = "token";
    /**
     * 交易返回的结构体数据；
     */
    public static final String RESULT_DATA = "data";
    public static final String RESULT_MSG = "msg";
    public static final String RESULT_CODE = "code";
    public static final Integer RESULT_SC = 10000000;
    public static final Integer RESULT_FI = -1;
    /**
     * 数据分隔点信息；
     */
    public static final String SPLIT_MARK = "_";
    public static final String SPLIT_MARK2 = "-";

}
