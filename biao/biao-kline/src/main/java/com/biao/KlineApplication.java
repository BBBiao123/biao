package com.biao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The type Kline application.
 *
 *  ""
 */
@SpringBootApplication
@MapperScan("com.biao.dao")
public class KlineApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(KlineApplication.class, args);
    }
}
