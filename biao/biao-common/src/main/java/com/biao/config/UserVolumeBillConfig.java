package com.biao.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * UserVolumeBillConfig.
 * <p>
 * 关于bill相关的定时器操作.
 * <p>
 * 19-1-2下午5:55
 *
 *  "" sixh
 */

@Configuration
@ConfigurationProperties(prefix = "user.coin-volume-bill")
@Data
public class UserVolumeBillConfig {

    private Integer needs;

    private Integer need;

    private Integer time;

    private Boolean taskFlag;
}
