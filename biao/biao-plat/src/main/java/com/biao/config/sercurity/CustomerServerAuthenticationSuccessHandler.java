package com.biao.config.sercurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

public interface CustomerServerAuthenticationSuccessHandler extends ServerAuthenticationSuccessHandler {

    Mono<Void> onAuthenticationSuccess(Authentication token, WebFilterExchange webFilterExchange,
                                       Authentication authentication);
}
