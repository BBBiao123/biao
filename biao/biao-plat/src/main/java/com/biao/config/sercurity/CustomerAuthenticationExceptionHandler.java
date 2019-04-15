package com.biao.config.sercurity;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface CustomerAuthenticationExceptionHandler {

    Mono<Void> handleException(ServerWebExchange exchange, Exception exception);
}
