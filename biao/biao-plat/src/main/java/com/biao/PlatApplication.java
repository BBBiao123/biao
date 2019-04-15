package com.biao;

import com.biao.configuration.redis.RedisConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.config.EnableWebFlux;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableWebFlux
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.biao.mapper")
@Import(value = {RedisConfiguration.class})
@ComponentScan(value = "com.biao")
public class PlatApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlatApplication.class, args);
    }
}
