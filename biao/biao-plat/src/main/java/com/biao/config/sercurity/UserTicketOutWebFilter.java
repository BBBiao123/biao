package com.biao.config.sercurity;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class UserTicketOutWebFilter implements WebFilter {

    private CustomerServerAccessDeniedHandler accessDecisionManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .as(authentication -> this.accessDecisionManager.verify(authentication, exchange))
                .switchIfEmpty(chain.filter(exchange));
    }

    public void setAccessDecisionManager(CustomerServerAccessDeniedHandler accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

}
