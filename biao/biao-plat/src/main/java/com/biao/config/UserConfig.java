package com.biao.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@ConfigurationProperties(prefix = "user.config")
@Data
public class UserConfig {

    /**
     * 锁定长度  时间 小时
     */
    private Integer lockLength;

    private BigDecimal secretFreeQuota;

    private Integer lastVersion;

    private Integer isForceUpdate;

    private String androidUrl;

    private String iosUrl;

    private BigDecimal exRate;

    public LocalDateTime plusHours() {
        return LocalDateTime.now().plusHours(this.lockLength == null ? 24 : this.lockLength);
    }
}
