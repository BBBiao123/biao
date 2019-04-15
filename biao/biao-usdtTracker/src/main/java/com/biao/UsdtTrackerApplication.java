package com.biao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan(value = "com.biao")
@MapperScan(basePackages = "com.biao.mapper")
@EnableTransactionManagement
@RestController
@RequestMapping("/biao")
public class UsdtTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsdtTrackerApplication.class, args);
    }
    @GetMapping("/index")
    public String kline() {
        return "hello";
    }


}
