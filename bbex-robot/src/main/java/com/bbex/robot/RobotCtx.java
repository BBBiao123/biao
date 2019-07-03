package com.bbex.robot;

import lombok.Data;

/**
 * 机器人配置信息；
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
     * toekn处理；
     */
    private String token;
    /**
     * 处理的url;
     */
    private String url;
    /**
     * 登录的帐号；
     */
    private String loginUser;
    /**
     * 登录的密码；
     */
    private String loginPass;
    /**
     * 连接远程的apiKey;
     */
    private String apiKey;
    /**
     * 连接远程skey;
     */
    private String skey;
}
