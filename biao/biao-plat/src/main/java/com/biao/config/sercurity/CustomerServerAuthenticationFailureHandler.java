package com.biao.config.sercurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

public interface CustomerServerAuthenticationFailureHandler extends ServerAuthenticationFailureHandler {

    Mono<Void> onAuthenticationFailure(Authentication token, WebFilterExchange webFilterExchange, Exception exception);
}
