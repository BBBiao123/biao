package com.biao;

import com.biao.configuration.redis.RedisConfiguration;
import com.biao.constant.Constant;
import com.biao.wallet.ethereum.Token;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.stream.Collectors;

import static  com.biao.constant.Constant.*;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@Import(value = {RedisConfiguration.class})
@ComponentScan(value = "com.biao")
@MapperScan(basePackages = "com.biao.mapper")
@EnableTransactionManagement
public class Application {
    public static void main(String[] args) {
        String symbol_mg = "MG";
        String contractAddress_mg = "0x10ea970cd621fab8553551030db5f9787958d422";
        String symbol_bat = "BAT";
        String contractAddress_bat ="0x0d8775f648430679a709e98d2b0cb6250d2887ef";
        String symbol_zrx = "ZRX";
        String contractAddress_zrx ="0xe41d2489571d322189246dafa5ebde1f4699f498";
        TOKEN_ADDRESS_MAP.put(symbol_mg, contractAddress_mg);
        TOKEN_ADDRESS_MAP.put(symbol_bat, contractAddress_bat);
        TOKEN_ADDRESS_MAP.put(symbol_zrx, contractAddress_zrx);
        Token token_mg = new Token(contractAddress_mg,3);
        Token token_bat = new Token(contractAddress_bat,18);
        Token token_zrx = new Token(contractAddress_zrx,18);
        TOKEN_MAP.put(symbol_mg,token_mg);
        TOKEN_MAP.put(symbol_bat,token_bat);
        TOKEN_MAP.put(symbol_zrx,token_zrx);


        Constant.TOKEN_LIST = TOKEN_ADDRESS_MAP.entrySet().stream()
                .map(x -> new Token(x.getKey(), x.getValue())
                ).collect(Collectors.toList());
        SpringApplication stun = new SpringApplication(Application.class);
        stun.setWebApplicationType(WebApplicationType.NONE);
        stun.run(args);

    }


}
