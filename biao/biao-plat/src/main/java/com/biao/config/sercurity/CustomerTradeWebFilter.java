package com.biao.config.sercurity;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class CustomerTradeWebFilter implements WebFilter {

    private CustomerTradeAccessDeniedHandler accessDecisionManager;

    private ServerWebExchangeMatcher requiresAuthenticationMatcher = ServerWebExchangeMatchers.anyExchange();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return this.requiresAuthenticationMatcher.matches(exchange)
                .filter(matchResult -> matchResult.isMatch())
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(matchResult -> ReactiveSecurityContextHolder.getContext()
                        .filter(c -> c.getAuthentication() != null)
                        .map(SecurityContext::getAuthentication)
                        .as(authentication -> this.accessDecisionManager.verify(authentication, exchange))
                        .switchIfEmpty(chain.filter(exchange)))
                ;
    }

    public void setRequiresAuthenticationMatcher(ServerWebExchangeMatcher requiresAuthenticationMatcher) {
        this.requiresAuthenticationMatcher = requiresAuthenticationMatcher;
    }

    public void setAccessDecisionManager(CustomerTradeAccessDeniedHandler accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

}
