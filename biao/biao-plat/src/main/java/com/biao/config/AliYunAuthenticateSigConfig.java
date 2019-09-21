package com.biao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aliyun.authenticatesig")
public class AliYunAuthenticateSigConfig {

    private String accessKeyId;

    private String accessKeySecret;

    private String otcAccessKeyId;

    private String otcAccessKeySecret;

    private String  appKey;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getOtcAccessKeyId() {
        return otcAccessKeyId;
    }

    public void setOtcAccessKeyId(String otcAccessKeyId) {
        this.otcAccessKeyId = otcAccessKeyId;
    }

    public String getOtcAccessKeySecret() {
        return otcAccessKeySecret;
    }

    public void setOtcAccessKeySecret(String otcAccessKeySecret) {
        this.otcAccessKeySecret = otcAccessKeySecret;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
