package com.biao.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "biao")
@Data
public class MailConfig {

	private List<MailConfigInfo> mail ;
}
