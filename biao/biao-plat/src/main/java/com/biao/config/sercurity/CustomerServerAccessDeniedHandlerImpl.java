package com.biao.config.sercurity;

import com.biao.constant.Constants;
import com.biao.constant.SercurityConstant;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component("customerServerAccessDeniedHandler")
public class CustomerServerAccessDeniedHandlerImpl implements CustomerServerAccessDeniedHandler {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valOpsStr;

    private int seesionSize = 1;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
        return Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    DataBuffer buffer = dataBufferFactory.wrap(e.getMessage().getBytes(
                            Charset.defaultCharset()));
                    return response.writeWith(Mono.just(buffer))
                            .doOnError(error -> DataBufferUtils.release(buffer));
                });
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, CustomerAuthException denied) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        DataBufferFactory bufferFactory = response.bufferFactory();
        if (denied instanceof CustomerAuthTradeException) {
            return response.writeWith(Mono.just(bufferFactory.wrap(JsonUtils.getJsonBytes(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_GOOGLE_CODE, "请进行谷歌验证")))));
        }
        return response.writeWith(Mono.just(bufferFactory.wrap(JsonUtils.getJsonBytes(GlobalMessageResponseVo.newInstance(denied.getCode(), "用户被踢出")))));
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, ServerWebExchange object) {
        return authentication.map(a -> this.verifyAuth(a))
                .defaultIfEmpty(new AuthorizationDecision(true));
    }

    private AuthorizationDecision verifyAuth(Authentication authentication) {
        if (authentication == null) {
            return new AuthorizationDecision(true);
        }
        if (!authentication.isAuthenticated()) {
            return new AuthorizationDecision(true);
        }
        RedisSessionUser user = (RedisSessionUser) authentication.getPrincipal();
        String userId = user.getId();
        if (RedisSessionUser.getIGNORE_USERIDS().contains(userId)) {
            return new AuthorizationDecision(true);
        }
        String token = user.getToken();
        //用户所以token集合
        String collectKey = SercurityConstant.redisUserCollectKey(userId);
        //判断用户是否有踢出标识
        String ticketoutTag = SercurityConstant.redisTicketOutKey(token);
        String authStirng = valOpsStr.get(ticketoutTag);
        List<String> tokens = stringRedisTemplate.opsForList().range(collectKey, 0, -1);
        if (!tokens.contains(token) && (StringUtils.isBlank(authStirng) || !"true".equals(authStirng))) {
            stringRedisTemplate.opsForList().leftPush(collectKey, token);
        }
        tokens = stringRedisTemplate.opsForList().range(collectKey, 0, -1);
        //判断是否有多个用户同事登录
        if (tokens.size() > seesionSize) {
            //开始踢出用户
            String ticketoutToken = stringRedisTemplate.opsForList().rightPop(collectKey);
            if (!token.equals(ticketoutToken)) {
                valOpsStr.set(SercurityConstant.redisTicketOutKey(ticketoutToken), "true");
                stringRedisTemplate.expire(SercurityConstant.redisTicketOutKey(ticketoutToken), SercurityConstant.REDIS_EXPIRE_TIME_SESSION, TimeUnit.SECONDS);
            }
        }
        //用户被踢出
        if (StringUtils.isNotBlank(authStirng) && authStirng.equals("true")) {
            //这里不要删除，避免客户端没有清除stoken，导致一直相互踢用户
            //stringRedisTemplate.delete(ticketoutTag);
            //清除踢出的用户信息
            String userRedisKey = SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + token;
            stringRedisTemplate.delete(userRedisKey);
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(true);
    }

    public void setSeesionSize(int seesionSize) {
        this.seesionSize = seesionSize;
    }

}
