package com.biao.config.sercurity;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class CustomerExceptionWebFilter implements WebFilter {

    private CustomerServerAccessDeniedHandler accessDeniedHandler;

    private ServerAuthenticationEntryPoint authenticationEntryPoint = new HttpBasicServerAuthenticationEntryPoint();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(CustomerAuthException.class, denied -> exchange.getPrincipal()
                        .switchIfEmpty(commenceAuthentication(exchange, denied))
                        .flatMap(principal -> this.accessDeniedHandler.handle(exchange, denied))
                );
    }

    private <T> Mono<T> commenceAuthentication(ServerWebExchange exchange, CustomerAuthException denied) {
        return this.authenticationEntryPoint.commence(exchange, new AuthenticationCredentialsNotFoundException("Not Authenticated", denied))
                .then(Mono.empty());
    }

    public void setAccessDeniedHandler(CustomerServerAccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }
}
