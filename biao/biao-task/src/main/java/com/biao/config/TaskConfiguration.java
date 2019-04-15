package com.biao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/21 下午5:55
 * @since JDK 1.8
 */
@Configuration
public class TaskConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}
