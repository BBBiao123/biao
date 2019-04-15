package com.biao.config.sercurity;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.ServerHttpBasicAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.biao.constant.SercurityConstant;
import com.biao.util.JsonUtils;

import reactor.core.publisher.Mono;

@Component
public class CustomerServerHttpBasicAuthenticationConverter extends ServerHttpBasicAuthenticationConverter {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Override
    public Mono<Authentication> apply(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String authorizationToken = request.getHeaders().getFirst(SercurityConstant.SESSION_TOKEN_HEADER);
        if (authorizationToken == null) {
            //解析请求参数是否携带token
            authorizationToken = request.getQueryParams() != null ? request.getQueryParams().getFirst(SercurityConstant.SESSION_TOKEN_HEADER) : null;
            //return exchange.getFormData().map(map->this.createAuthentication(map));
        }
        if (StringUtils.isBlank(authorizationToken)) {
            return Mono.empty();
        }
        Authentication authentication = getAuthenticationByToken(authorizationToken);
        if (authentication == null) {
            return Mono.empty();
        }
        return Mono.just(authentication);
    }

    private Authentication getAuthenticationByToken(String authorizationToken) {
        String sessionKey = SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + authorizationToken;
        if (!stringRedisTemplate.hasKey(sessionKey)) {
            return null;
        }
        String redisUser = (String) stringRedisTemplate.opsForHash().get(sessionKey, SercurityConstant.SESSION_TOKEN_REDIS_USER);
        if (StringUtils.isBlank(redisUser)) {
            return null;
        }
        RedisSessionUser user = JsonUtils.fromJson(redisUser, RedisSessionUser.class);
        stringRedisTemplate.expire(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + authorizationToken, SercurityConstant.REDIS_EXPIRE_TIME_SESSION, TimeUnit.SECONDS);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        return authentication;
    }
}
