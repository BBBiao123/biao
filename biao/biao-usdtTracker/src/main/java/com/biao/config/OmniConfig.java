package com.biao.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OmniConfig.
 */
@ConfigurationProperties(prefix = "omni.rpc")
@Getter
@Setter
@Component
public class OmniConfig {
    private String uri;
    private String username;
    private String password;
    private String sendAddr;
    private String collectAddr;
    private String feeAddr;
    private Double collectFee;
    private String coinId;
    private Integer syncTime;
    private Integer sendTime;
    private Integer collectTime;
}
