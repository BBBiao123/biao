package com.biao;

import com.biao.configuration.redis.RedisConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@Configuration
@ComponentScan("com.biao")
@MapperScan(basePackages = "com.biao.mapper")
@Import(value = {RedisConfiguration.class})
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }
}
