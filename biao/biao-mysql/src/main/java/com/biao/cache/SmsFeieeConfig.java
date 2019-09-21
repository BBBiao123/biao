package com.biao.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@ConfigurationProperties(prefix = "sms.config")
@Data
public class SmsFeieeConfig {

    /**
     * 锁定长度  时间 小时
     */
    private String account;

    private String pwd;

    private String signId;
    private String templateId;
    private String url;

}
