package com.biao.config.sercurity;

import com.biao.constant.Constants;
import com.biao.constant.SercurityConstant;
import com.biao.entity.PlatUser;
import com.biao.enums.MessageTemplateCode;
import com.biao.enums.VerificationCodeType;
import com.biao.google.GoogleAuthenticator;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.MessageSendService;
import com.biao.service.PlatUserService;
import com.biao.service.SmsMessageService;
import com.biao.util.DateUtil;
import com.biao.util.JsonUtils;
import com.biao.util.RsaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component("customerTradeAccessDeniedHandler")
public class CustomerTradeAccessDeniedHandlerImpl implements CustomerTradeAccessDeniedHandler {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PlatUserService platUserService;
    @Autowired
    private MessageSendService messageSendService;
    @Autowired
    private SmsMessageService smsMessageService;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valOpsStr;

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
        return response.writeWith(Mono.just(bufferFactory.wrap(JsonUtils.getJsonBytes(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_GOOGLE_CODE, "请进行谷歌验证")))));
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, ServerWebExchange object) {
        return authentication.map(a -> this.verifyAuth(a, object.getRequest()))
                .defaultIfEmpty(new AuthorizationDecision(true));
    }

    public AuthorizationDecision verifyAuth(Authentication authentication, ServerHttpRequest request) {
        if (authentication == null) {
            return new AuthorizationDecision(false);
        }
        if (!authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        RedisSessionUser user = (RedisSessionUser) authentication.getPrincipal();
        if (RedisSessionUser.getIGNORE_USERIDS().contains(user.getId())) {
            return new AuthorizationDecision(true);
        }
        String authKey = SercurityConstant.AUTH_USERNAME_REDIS_NAMESAPCE + user.getUsername();
        String authStirng = valOpsStr.get(authKey);
        if (StringUtils.isNotBlank(authStirng) && SercurityConstant.AuthType.PASS_AUTH.getCode().equals(authStirng)) {
            return new AuthorizationDecision(true);
        }
        //验证交易
        return validTradePass(request.getQueryParams(), user);
    }


    private AuthorizationDecision validTradePass(MultiValueMap<String, String> data, RedisSessionUser user) {
        //查询交易类型是否有验证  1：谷歌验证  2：交易密码验证 3:短信 4:邮箱
//        Integer exValidType = user.getExValidType();
//        if (exValidType == null) {
//            return new AuthorizationDecision(false);
//        }
//        if (exValidType == 1 && StringUtils.isBlank(user.getGoogleAuth())) {
//            return new AuthorizationDecision(false);
//        }
//        if (exValidType == 3 && StringUtils.isBlank(user.getMobile())) {
//            return new AuthorizationDecision(false);
//        }
//        if (exValidType == 4 && StringUtils.isBlank(user.getMail())) {
//            return new AuthorizationDecision(false);
//        }
//        List<String> exPasswords = data.get("exPassword");
//        if (exPasswords != null && StringUtils.isNotBlank(exPasswords.get(0))) {
//            boolean validResult = false;
//            String userId = user.getId();
//            String decodeExPassword = exPasswords.get(0);
//            String validTimeKey = new StringBuilder("c2s:trade:sell:")
//                    .append(DateUtil.formatDate()).append(":").append(userId).toString();
//            Long errorTime = valOpsStr.increment(validTimeKey, 1);
//            valOpsStr.getOperations().expire(validTimeKey, 3600 * 24, TimeUnit.SECONDS);
//            if (errorTime > 5) {
//                return new AuthorizationDecision(false);
//            }
//            String decryPassword = RsaUtils.decryptByPrivateKey(decodeExPassword, RsaUtils.DEFAULT_PRIVATE_KEY);
//            if (exValidType == 1) {
//                //谷歌验证      查询最新用户  后台会清空谷歌
//                PlatUser platUser = platUserService.findById(userId);
//                if (StringUtils.isBlank(platUser.getGoogleAuth())) {
//                    //谷歌验证
//                    return new AuthorizationDecision(false);
//                }
//                GoogleAuthenticator ga = new GoogleAuthenticator();
//                ga.setWindowSize(5);
//                validResult = ga.checkCode(platUser.getGoogleAuth(), Long.parseLong(decryPassword), System.currentTimeMillis());
//            } else if (exValidType == 3) {
//                //短信验证
//                validResult = smsMessageService.validSmsCode(user.getMobile(), MessageTemplateCode.MOBILE_EX_TRADE_TEMPLATE.getCode(), decryPassword);
//            } else if (exValidType == 4) {
//                //邮箱验证
//                try {
//                    messageSendService.mailValid(MessageTemplateCode.EMAIL_EX_TRADE_TEMPLATE.getCode(), VerificationCodeType.EX_TRADE_MAIL, user.getMail(), decryPassword);
//                    validResult = true;
//                } catch (Exception e) {
//                    validResult = false;
//                }
//            }
//            if (validResult) {
//                //短信验证
//                String authKey = SercurityConstant.AUTH_USERNAME_REDIS_NAMESAPCE + user.getUsername();
//                valOpsStr.set(authKey, SercurityConstant.AuthType.PASS_AUTH.getCode(), SercurityConstant.REDIS_EXPIRE_TIME_ONE_HOUR * 2, TimeUnit.SECONDS);
//            }
//            return new AuthorizationDecision(validResult);
//        }
//        return new AuthorizationDecision(false);
        return new AuthorizationDecision(true);
    }

}
