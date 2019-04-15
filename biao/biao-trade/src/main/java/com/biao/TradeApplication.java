package com.biao;

import com.biao.configuration.redis.RedisConfiguration;
import com.biao.spring.SpringBeanFactoryContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
public class TradeApplication {
    public static void main(String[] args) {
        SpringApplication stun = new SpringApplication(TradeApplication.class);
        stun.setWebApplicationType(WebApplicationType.NONE);
        stun.run(args);
    }
}
