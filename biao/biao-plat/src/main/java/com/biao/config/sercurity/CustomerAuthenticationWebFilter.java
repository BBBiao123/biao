package com.biao.config.sercurity;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.ServerHttpBasicAuthenticationConverter;
import org.springframework.security.web.server.WebFilterExchange;
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

public class CustomerAuthenticationWebFilter implements WebFilter {

    private final ReactiveAuthenticationManager authenticationManager;

    private CustomerAuthenticationExceptionHandler authenticationExceptionHandler;

    private CustomerServerAuthenticationSuccessHandler authenticationSuccessHandler = new CustomerWebFilterChainServerAuthenticationSuccessHandler();

    private Function<ServerWebExchange, Mono<Authentication>> authenticationConverter = new ServerHttpBasicAuthenticationConverter();

    private ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();

    private ServerWebExchangeMatcher requiresAuthenticationMatcher = ServerWebExchangeMatchers.anyExchange();

    public CustomerAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return this.requiresAuthenticationMatcher.matches(exchange)
                .filter(matchResult -> matchResult.isMatch())
                .flatMap(matchResult -> this.authenticationConverter.apply(exchange))
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(token -> authenticate(exchange, chain, token))
                .onErrorResume(Exception.class, e -> this.authenticationExceptionHandler.handleException(exchange, e));
    }

    private Mono<Void> authenticate(ServerWebExchange exchange,
                                    WebFilterChain chain, Authentication token) {
        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
        return this.authenticationManager.authenticate(token)
                .flatMap(authentication -> onAuthenticationSuccess(token, authentication, webFilterExchange))
                ;
    }

    private Mono<Void> onAuthenticationSuccess(Authentication token, Authentication authentication, WebFilterExchange webFilterExchange) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return this.securityContextRepository.save(exchange, securityContext)
                .then(this.authenticationSuccessHandler
                        .onAuthenticationSuccess(token, webFilterExchange, authentication))
                .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    public void setSecurityContextRepository(
            ServerSecurityContextRepository securityContextRepository) {
        Assert.notNull(securityContextRepository, "securityContextRepository cannot be null");
        this.securityContextRepository = securityContextRepository;
    }

    public void setAuthenticationSuccessHandler(CustomerServerAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    public void setAuthenticationConverter(Function<ServerWebExchange, Mono<Authentication>> authenticationConverter) {
        this.authenticationConverter = authenticationConverter;
    }

    public void setRequiresAuthenticationMatcher(
            ServerWebExchangeMatcher requiresAuthenticationMatcher) {
        Assert.notNull(requiresAuthenticationMatcher, "requiresAuthenticationMatcher cannot be null");
        this.requiresAuthenticationMatcher = requiresAuthenticationMatcher;
    }

    public void setAuthenticationExceptionHandler(CustomerAuthenticationExceptionHandler authenticationExceptionHandler) {
        this.authenticationExceptionHandler = authenticationExceptionHandler;
    }
}
