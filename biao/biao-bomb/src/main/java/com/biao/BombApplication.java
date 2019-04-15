package com.biao;

import com.biao.configuration.redis.RedisConfiguration;
import com.biao.utils.BombService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Configuration
@ComponentScan("com.biao")
@Import(value = {RedisConfiguration.class})
public class
BombApplication {

    public static void main(String[] args) {
        SpringApplication.run(BombApplication.class, args);
        BombService.start();
    }

}
