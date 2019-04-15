package com.biao.rebot.config;

import lombok.Data;

/**
 * RedisConfig.
 * <p>
 *     redis相关信息.
 * <p>
 * 18-12-19上午11:02
 *
 *  "" sixh
 */
@Data
public class RedisConfig {
    /**
     * 地址.
     */
    private String host;

    /**
     * 密码.
     */
    private String password;

    /**
     * redis的使用模式.
     */
    private String mode;

    /**
     * 在使用哨兵模式的时候进行配置.
     */
    private String master;
}
