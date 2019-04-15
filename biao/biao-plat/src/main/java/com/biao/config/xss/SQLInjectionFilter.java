package com.biao.config.xss;

import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.regex.Pattern;

public class SQLInjectionFilter implements WebFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQLInjectionFilter.class);

    private ServerWebExchangeMatcher requiresAuthenticationMatcher = ServerWebExchangeMatchers.anyExchange();

    private static final String regularExpression = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|" + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

    private static Pattern sqlPattern = Pattern.compile(regularExpression, Pattern.CASE_INSENSITIVE);

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        return this.requiresAuthenticationMatcher.matches(serverWebExchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(webFilterChain.filter(serverWebExchange).then(Mono.empty()))
                .flatMap(matchResult -> {
                    //进行验证
                    final ServerHttpRequest request = serverWebExchange.getRequest();
                    final MultiValueMap<String, String> queryParams = request.getQueryParams();
                    if (queryParams.size() > 0) {
                        for (Object o : queryParams.entrySet()) {
                            Map.Entry entry = (Map.Entry) o;
                            String value = String.valueOf(entry.getValue());
                            if (StringUtils.isNoneBlank(value)) {
                                if (sqlPattern.matcher(value).find()) {
                                    LOGGER.error("#疑似SQL注入攻击！参数名称：{}；录入信息:{}", entry.getKey(), value);
                                    return Mono.just(false);
                                }
                            }
                        }
                    }
                    final HttpMethod method = request.getMethod();
                    if (HttpMethod.POST == method) {
                        return serverWebExchange.getFormData().map(m -> {
                            for (Object o : m.entrySet()) {
                                Map.Entry entry = (Map.Entry) o;
                                String value = String.valueOf(entry.getValue());
                                if (StringUtils.isNoneBlank(value)) {
                                    if (sqlPattern.matcher(value).find()) {
                                        LOGGER.error("#疑似SQL注入攻击！参数名称：{}；录入信息:{}", entry.getKey(), value);
                                        return false;
                                    }
                                }
                            }
                            return true;
                        });
                    }
                    return Mono.just(true);
                })
                .flatMap(results -> results ? webFilterChain.filter(serverWebExchange) : write(serverWebExchange));
    }

    private Mono<Void> write(ServerWebExchange serverWebExchange) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        final GlobalMessageResponseVo result = GlobalMessageResponseVo.newErrorInstance("您输入的参数有非法字符，请输入正确的参数！");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(JsonUtils.toJson(result).getBytes())));
    }

    public void setRequiresAuthenticationMatcher(ServerWebExchangeMatcher requiresAuthenticationMatcher) {
        this.requiresAuthenticationMatcher = requiresAuthenticationMatcher;
    }
}