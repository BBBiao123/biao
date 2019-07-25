package com.biao;

import com.biao.configuration.redis.RedisConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@Import(value = {RedisConfiguration.class})
@ComponentScan(value = "com.biao")
@MapperScan(basePackages = "com.biao.mapper")
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication stun = new SpringApplication(Application.class);
        stun.setWebApplicationType(WebApplicationType.NONE);
        stun.run(args);
    }


}
