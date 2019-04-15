package com.biao.rebot.config;

import lombok.Data;

/**
 * 机器人配置信息；
 *
 *
 */
@Data
public class RobotCtx {
    /**
     * 交易的时间；
     */
    private Integer tradeTime = 5;

    /**
     * volume工厂的处理；
     */
    private String volumeFactory;

    /**
     * 价格的工厂实现；
     */
    private String priceFactory;

    /**
     * 价格服务处理.
     */
    private String priceService;

    /**
     * jdbcurl;
     */

    private String jdbcUrl;

    /**
     * jdbcUser
     */
    private String jdbcUser;

    /**
     * jdbcPass
     */
    private String jdbcPass;
    /**
     * 处理的url;
     */
    private String url;

    /**
     * 连接远程的apiKey;
     */
    private String apiKey;

    /**
     * 连接远程skey;
     */
    private String skey;

    /**
     * 代理服务器.
     */
    private Proxy proxy;

    /**
     * redis消息.
     */
    private RedisConfig redis;
}
