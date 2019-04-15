package com.biao.config;

import com.biao.router.UsdtHttpRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.resources.LoopResources;

import java.util.Objects;

/**
 * HttpServerConfig.
 *
 *  ""
 */
@Configuration
public class HttpServerConfig {

    private final Environment environment;

    @Autowired
    public HttpServerConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public RouterFunction<ServerResponse> monoRouterFunction(UsdtHttpRouter usdtHttpRouter) {
        return usdtHttpRouter.routes();
    }

/*
    @Bean
    public HttpServer httpServer(RouterFunction<?> routerFunction) {
        HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer server = HttpServer.create(
                Objects.requireNonNull(environment.getProperty("server.address")),
                Integer.valueOf(Objects.requireNonNull(environment.getProperty("server.port"))));
        return server;
    }
*/

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {
        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory();
        LoopResources resources = LoopResources.create("webflux-http", 8, true);
        ReactorResourceFactory reactorResourceFactory = new ReactorResourceFactory();
        reactorResourceFactory.setLoopResources(resources);
        factory.setResourceFactory(reactorResourceFactory);
        factory.addServerCustomizers(config -> config
                .port(Integer.valueOf(Objects.requireNonNull(environment.getProperty("server.port"))))
                .host(Objects.requireNonNull(environment.getProperty("server.address"))));
        return factory;
    }
}
