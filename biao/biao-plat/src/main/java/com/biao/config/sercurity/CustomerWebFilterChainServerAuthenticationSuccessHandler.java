package com.biao.config.sercurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomerWebFilterChainServerAuthenticationSuccessHandler implements CustomerServerAuthenticationSuccessHandler {
    @Override
    public Mono<Void> onAuthenticationSuccess(Authentication token, WebFilterExchange webFilterExchange,
                                              Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        return webFilterExchange.getChain().filter(exchange);
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        return webFilterExchange.getChain().filter(exchange);
    }
}
