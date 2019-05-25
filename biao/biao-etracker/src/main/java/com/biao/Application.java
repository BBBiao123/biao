package com.biao;

import com.biao.configuration.redis.RedisConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@Import(value = {RedisConfiguration.class})
@ComponentScan(value = "com.biao")
@MapperScan(basePackages = "com.biao.mapper")
@EnableTransactionManagement
public class Application {
    public static void main(String[] args) {

//        String symbol = "UES";
//        String contractAddress = "0x479Bb409743D07cF47e2cf692BfF62d174e72AF1";
//        TOKEN_ADDRESS_MAP.put(symbol, contractAddress);
//        Token token = new Token(contractAddress,18);
//        TOKEN_MAP.put(symbol,token);
//
//
//        Constant.TOKEN_LIST = TOKEN_ADDRESS_MAP.entrySet().stream()
//                .map(x -> new Token(x.getKey(), x.getValue())
//                ).collect(Collectors.toList());
        SpringApplication stun = new SpringApplication(Application.class);
        stun.setWebApplicationType(WebApplicationType.NONE);
        stun.run(args);

    }


}
