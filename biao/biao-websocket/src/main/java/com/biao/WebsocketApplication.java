package com.biao;

import com.biao.configuration.redis.RedisConfiguration;
import com.biao.netty.bootstrap.NettyServerBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import tk.mybatis.spring.annotation.MapperScan;

/**
 *  ""(Myth)
 */
@SpringBootApplication
@MapperScan(basePackages = "com.biao.mapper")
@Import(value = {RedisConfiguration.class})
@ComponentScan(value = "com.biao")
public class WebsocketApplication {

    public static void main(String[] args) {
        SpringApplication stun = new SpringApplication(WebsocketApplication.class);
        stun.setWebApplicationType(WebApplicationType.NONE);
        final ConfigurableApplicationContext context = stun.run(args);
        final NettyServerBootstrap nettyServerBootstrap = context.getBean(NettyServerBootstrap.class);
        try {
            nettyServerBootstrap.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
