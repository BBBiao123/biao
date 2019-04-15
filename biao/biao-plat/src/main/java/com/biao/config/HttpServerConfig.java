package com.biao.config;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import reactor.netty.resources.LoopResources;

/**
 * HttpServerConfig.
 *
 *  ""1
 */
@Configuration
public class HttpServerConfig {

    private static final int threads = 300;

    private static final int selectThreads = Runtime.getRuntime().availableProcessors() << 1;

    private final String THREAD_NAME = "biao-webflux-http";

    /**
     * Reactive web server factory reactive web server factory.
     *
     * @return the reactive web server factory
     */
    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {
        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory();
        LoopResources resources = LoopResources.create(THREAD_NAME, selectThreads, threads, false);
        ReactorResourceFactory reactorResourceFactory = new ReactorResourceFactory();
        reactorResourceFactory.setLoopResources(resources);
        factory.setResourceFactory(reactorResourceFactory);
        return factory;
    }

}
