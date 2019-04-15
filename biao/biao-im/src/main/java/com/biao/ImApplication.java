package com.biao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * im start
 *
 *  ""
 */
@SpringBootApplication
public class ImApplication {
    public static void main(String[] args) {
        SpringApplication stun = new SpringApplication(ImApplication.class);
        stun.setWebApplicationType(WebApplicationType.NONE);
        stun.run(args);
    }
}
