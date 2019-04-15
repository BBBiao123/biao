package com.biao.config.sercurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.ServerHttpBasicAuthenticationConverter;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class TokenAuthWebFilter implements WebFilter {

    private Function<ServerWebExchange, Mono<Authentication>> authenticationConverter = new ServerHttpBasicAuthenticationConverter();

    private ServerWebExchangeMatcher requiresAuthenticationMatcher = ServerWebExchangeMatchers.anyExchange();

    private ServerAuthenticationFailureHandler authenticationFailureHandler = new ServerAuthenticationEntryPointFailureHandler(new HttpBasicServerAuthenticationEntryPoint());

    private ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();

    private ServerAuthenticationSuccessHandler authenticationSuccessHandler = new WebFilterChainServerAuthenticationSuccessHandler();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return this.requiresAuthenticationMatcher.matches(exchange).filter(matchResult -> matchResult.isMatch())
                .flatMap(matchResult -> this.authenticationConverter.apply(exchange))
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(token -> authenticate(exchange, chain, token));
    }

    private Mono<Void> authenticate(ServerWebExchange exchange,
                                    WebFilterChain chain, Authentication token) {
        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
        return this.authenticate(token).flatMap(authentication -> onAuthenticationSuccess(authentication, webFilterExchange))
                .onErrorResume(AuthenticationException.class, e -> this.authenticationFailureHandler
                        .onAuthenticationFailure(webFilterExchange, e));
    }

    private Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return this.securityContextRepository.save(exchange, securityContext)
                .then(this.authenticationSuccessHandler
                        .onAuthenticationSuccess(webFilterExchange, authentication))
                .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    private Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication);
    }

    public void setAuthenticationConverter(Function<ServerWebExchange, Mono<Authentication>> authenticationConverter) {
        this.authenticationConverter = authenticationConverter;
    }

    public void setRequiresAuthenticationMatcher(ServerWebExchangeMatcher requiresAuthenticationMatcher) {
        Assert.notNull(requiresAuthenticationMatcher, "requiresAuthenticationMatcher cannot be null");
        this.requiresAuthenticationMatcher = requiresAuthenticationMatcher;
    }
}
