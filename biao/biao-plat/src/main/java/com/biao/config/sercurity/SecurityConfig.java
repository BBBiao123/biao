package com.biao.config.sercurity;

import com.biao.config.AliYunAuthenticateSigConfig;
import com.biao.config.xss.SQLInjectionFilter;
import com.biao.config.xss.XssFilter;
import com.biao.constant.Constants;
import com.biao.constant.SercurityConstant;
import com.biao.entity.PlatUser;
import com.biao.enums.MessageTemplateCode;
import com.biao.enums.VerificationCodeType;
import com.biao.exception.PlatException;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.reactive.data.mongo.disruptor.DisruptorData;
import com.biao.reactive.data.mongo.disruptor.DisruptorManager;
import com.biao.reactive.data.mongo.domain.UserLoginLog;
import com.biao.service.MessageSendService;
import com.biao.service.SmsMessageService;
import com.biao.util.DateUtil;
import com.biao.util.JsonUtils;
import com.biao.util.NumberUtils;
import com.biao.util.RsaUtils;
import com.biao.utils.AliYunAuthenticateSigUtils;
import com.biao.vo.AuthenticateSigVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerFormLoginAuthenticationConverter;
import org.springframework.security.web.server.ServerHttpBasicAuthenticationConverter;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Autowired
    private ReactiveUserDetailsService userDetailsService;
    @Autowired
    private ServerHttpBasicAuthenticationConverter authenticationConverter;
    /*@Autowired
    private ReactiveAuthorizationManager<AuthorizationContext> accessDecisionManager;
    */
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valOpsStr;
    @Autowired
    private CustomerServerAccessDeniedHandler customerServerAccessDeniedHandler;
    @Autowired
    private CustomerTradeAccessDeniedHandler customerTradeAccessDeniedHandler;
    @Autowired
    private AliYunAuthenticateSigConfig aliYunAuthenticateSigConfig;
    @Autowired
    private SmsMessageService smsMessageService;
    @Autowired
    private MessageSendService messageSendService;

    private static final String[] xxsUrl = new String[]{
            "/biao/user/register",
            "/biao/user/login",
            "/biao/user/googleBinder",
            "/biao/user/sendMobileSms",
            "/biao/user/updateMobileSms",
            "/biao/user/mobileBinder",
            "/biao/user/binderEmail",
            "/biao/user/updateUser",
            "/biao/user/updateUserMobile",
            "/biao/user/googleValid",
            "/biao/user/updatePassword",
            "/biao/mail/resetpwd",
            "/biao/mobile/resetpwd",
            "/biao/offline/**",
            "/biao/coin/volume/**",
            "/biao/withdraw/address/add"

    };


    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.csrf().disable()
                .addFilterAt(sqlInjectionFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
                .addFilterAt(xssFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
                .addFilterAt(createAuthenticationTokenFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
                .addFilterAt(createCustomerExceptionWebFilter(), SecurityWebFiltersOrder.EXCEPTION_TRANSLATION)
                .addFilterAt(createUserTicketOutWebFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .addFilterAt(createCustomerTradeWebFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .addFilterAt(createAuthenticationFilter(), SecurityWebFiltersOrder.FORM_LOGIN)
                .authenticationManager(createReactiveAuthenticationManager())
                .authorizeExchange().pathMatchers(
                        "/biao/user/register", "/biao/mail/sendCode", "/biao/mobile/sendCode",
                        "/biao/user/login", "/biao/user/loginValid", "/biao/user/mobile/resetpwd", "/biao/user/mail/resetpwd", "/biao/user/findUser",
                        "/biao/qrcode", "/biao/valid/createCode", "/biao/im/*",
                        "/biao/coin/**", "/biao/notice", "/biao/cms/**",
                        "/biao/mongo/**", "/biao/websocket/**", "/biao/home/**",
                        "/biao/mk2/teammining/list", "/biao/mk2/teammining/conf", "/biao/super/book/list", "/biao/otc/getLastRate",
                        "/biao/otc/coin/list", "/biao/otc/agent/**", "/biao/otc/admin/**", "/biao/otc/exchange/cust/paynologin", "/biao/otc/volume/admin/change","/biao/otc/volume/query",
                        "/biao/otc/exchange/getno", "/biao/otc/exchange/find", "/biao/otc/exchange/paynologin", "/biao/otc/volume/user", "/biao/otc/coin/convert/query",
                        "/biao/offline/coin/list", "/biao/offline/bank/get", "/biao/offline/bankInfo/**", "/biao/areaSell/*", "/biao/websocket/showView", "/biao/index/mainCoinCnb", "/biao/mk2/total/info",
                        "/biao/offline/gadvert/list", "/biao/index/**", "/biao/relay/**", "/biao/show/getData", "/biao/miner/recruit/list", "/biao/lucky/list", "/biao/ping","/biao/offline/advert/cancel/ll",
                        "/biao/trade/batchCancelTradeAbc","/biao/balance/**","/biao/user/login")
                .permitAll().and()
                .authorizeExchange().anyExchange().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(new CustomerServerAuthenticationEntryPoint()).and()
                .logout().logoutUrl("/biao/user/logout").logoutSuccessHandler(new CustomerServerLogoutSuccessHandler(stringRedisTemplate)).and()
                .build();
    }


    @Bean
    SQLInjectionFilter sqlInjectionFilter() {
        SQLInjectionFilter sqlInjectionFilter = new SQLInjectionFilter();
        sqlInjectionFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(xxsUrl));
        return sqlInjectionFilter;
    }

    @Bean
    XssFilter xssFilter() {
        XssFilter xssFilter = new XssFilter();
        xssFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(xxsUrl));
        return xssFilter;
    }


    @Bean
    UserTicketOutWebFilter createUserTicketOutWebFilter() {
        UserTicketOutWebFilter userTicketOutWebFilter = new UserTicketOutWebFilter();
        userTicketOutWebFilter.setAccessDecisionManager(customerServerAccessDeniedHandler);
        return userTicketOutWebFilter;
    }

    @Bean
    CustomerTradeWebFilter createCustomerTradeWebFilter() {
        CustomerTradeWebFilter customerTradeWebFilter = new CustomerTradeWebFilter();
        customerTradeWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/biao/trade/getOrderNo",
                "/biao/trade/buyIn", "/biao/trade/sellOut",
                "/biao/offline/sell", "/biao/otc/secendCheck",
                "/biao/offline/change/preConfirm", "/biao/offline/change/confirm",
                "/biao/red/envelope/send"));
        customerTradeWebFilter.setAccessDecisionManager(customerTradeAccessDeniedHandler);
        return customerTradeWebFilter;
    }

    @Bean
    CustomerExceptionWebFilter createCustomerExceptionWebFilter() {
        CustomerExceptionWebFilter customerExceptionWebFilter = new CustomerExceptionWebFilter();
        customerExceptionWebFilter.setAccessDeniedHandler(customerServerAccessDeniedHandler);
        return customerExceptionWebFilter;
    }

    @Bean
    ReactiveAuthenticationManager createReactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    @Bean
    CustomerAuthenticationWebFilter createAuthenticationFilter() {
        CustomerAuthenticationWebFilter authenticationFilter = new CustomerAuthenticationWebFilter(createReactiveAuthenticationManager());
        authenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/biao/user/login"));
        authenticationFilter.setAuthenticationConverter(new CustomerServerFormLoginAuthenticationConverter(valOpsStr, aliYunAuthenticateSigConfig));
        authenticationFilter.setAuthenticationExceptionHandler(new CustomerAuthenticationExceptionHandlerImpl(valOpsStr));
        authenticationFilter.setAuthenticationSuccessHandler(new CustomerServerAuthenticationSuccessHandlerImpl(valOpsStr, stringRedisTemplate, messageSendService, smsMessageService));
        return authenticationFilter;
    }

    @Bean
    TokenAuthWebFilter createAuthenticationTokenFilter() {
        TokenAuthWebFilter authenticationFilter = new TokenAuthWebFilter();
        authenticationFilter.setAuthenticationConverter(authenticationConverter);
        return authenticationFilter;
    }

    ServerFormLoginAuthenticationConverter createServerFormLoginAuthenticationConverter() {
        ServerFormLoginAuthenticationConverter serverFormLoginAuthenticationConverter = new ServerFormLoginAuthenticationConverter();
        serverFormLoginAuthenticationConverter.setPasswordParameter("password");
        serverFormLoginAuthenticationConverter.setUsernameParameter("username");
        return serverFormLoginAuthenticationConverter;
    }

    /**
     * 自定义用户退出处理
     *
     *  ""dministrator
     */
    static class CustomerServerLogoutSuccessHandler implements ServerLogoutSuccessHandler {

        private StringRedisTemplate stringRedisTemplate;


        public CustomerServerLogoutSuccessHandler(StringRedisTemplate stringRedisTemplate) {
            this.stringRedisTemplate = stringRedisTemplate;
        }

        @Override
        public Mono<Void> onLogoutSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
            ServerWebExchange exchange = webFilterExchange.getExchange();
            ServerHttpResponse result = exchange.getResponse();
            result.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            result.setStatusCode(HttpStatus.OK);
            Object logoutUser = authentication.getPrincipal();
            if (logoutUser instanceof RedisSessionUser) {
                RedisSessionUser user = (RedisSessionUser) authentication.getPrincipal();
                String authorizationToken = user.getToken();
                //清楚token
                stringRedisTemplate.delete(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + authorizationToken);
                // 发送消息
                //producer.send("topic.user.logout.message", user.getUsername(), user);
            }
            DataBufferFactory bufferFactory = result.bufferFactory();
            Mono<DataBuffer> authenticationUser = Mono.just(bufferFactory.wrap(JsonUtils.getJsonBytes(GlobalMessageResponseVo.newSuccessInstance("成功退出"))));
            return result.writeWith(authenticationUser);
        }

    }

    /**
     * 自定义登录失败异常处理
     *
     *  ""dministrator
     */
    static class CustomerAuthenticationExceptionHandlerImpl implements CustomerAuthenticationExceptionHandler {

        private ValueOperations<String, String> valOpsStr;

        private Integer defaultValidTime = 3;

        private Integer defaultMaxValidTime = 5;

        private String usernameParameter = "username";

        private List<Integer> saveLogCodes = Arrays.asList(Constants.LOGIN_ERROR_MORE_ERROR, Constants.PARAM_ERROR
                , Constants.VALIDCODE_IS_ERROR);

        public CustomerAuthenticationExceptionHandlerImpl(ValueOperations<String, String> valOpsStr) {
            this.valOpsStr = valOpsStr;
        }

        @Override
        public Mono<Void> handleException(ServerWebExchange exchange, Exception exception) {
            ServerHttpResponse result = exchange.getResponse();
            result.setStatusCode(HttpStatus.OK);
            result.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            Mono<DataBuffer> dataBuffer = exchange.getFormData().map(data -> this.handlerUserError(data, exception, exchange));
            return result.writeWith(dataBuffer);
        }

        private DataBuffer handlerUserError(MultiValueMap<String, String> data, Exception exception, ServerWebExchange exchange) {
            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
            int code = Constants.GLOBAL_ERROR_CODE;
            String message = null;
            Object errorData = null;
            String username = data.getFirst(this.usernameParameter);
            String source = data.getFirst("source");
            if (exception instanceof PlatException) {
                PlatException platException = (PlatException) exception;
                code = platException.getCode();
                message = platException.getMsg();
                if (saveLogCodes.contains(code)) {
                    //异步记录用户登录失败
                    SecurityConfig.saveUserLogin(exchange, username, source, message, null);
                }
            } else if (exception instanceof AuthenticationException) {
                if (exception instanceof LockedException) {
                    code = Constants.USERNAME_LOCKED_ERROR;
                }
                if (exception instanceof DisabledException) {
                    code = Constants.USERNAME_DISABLED_ERROR;
                }
                if (exception instanceof BadCredentialsException) {
                    code = Constants.USER_VALID_PASSWORD_ERROR;
                }
                AuthenticationException authenticationException = (AuthenticationException) exception;
                message = authenticationException.getMessage();
                //异步记录用户登录失败
                SecurityConfig.saveUserLogin(exchange, username, source, message, null);
            } else {
                return bufferFactory.wrap(JsonUtils.getJsonBytes(GlobalMessageResponseVo.newInstance(code, "非法请求")));
            }
            if (StringUtils.isNotBlank(username)) {
                //用户输入的登录名
                String validTimeKey = new StringBuilder(SercurityConstant.VALID_TIME_USERNAME_REDIS_NAMESAPCE)
                        .append(DateUtil.formatDate()).append(":").append(username).toString();
                String validTime = valOpsStr.get(validTimeKey);
                valOpsStr.getOperations().expire(validTimeKey, SercurityConstant.REDIS_EXPIRE_TIME_ONE_HOUR * 24, TimeUnit.SECONDS);
                if (StringUtils.isNotBlank(validTime) && Integer.parseInt(validTime) >= defaultValidTime) {
                    //增加验证码
                    code = Constants.ADD_VALID_CODE_ERROR;
                }
                if (StringUtils.isNotBlank(validTime) && Integer.parseInt(validTime) == defaultMaxValidTime) {
                    //发送短信提示用户
                    PlatUser platUser = new PlatUser();
                    platUser.setUsername(username);
                    com.biao.disruptor.DisruptorManager.instance().runConfig();
                    com.biao.disruptor.DisruptorData disData = new com.biao.disruptor.DisruptorData();
                    disData.setType(7);
                    disData.setPlatUser(platUser);
                    com.biao.disruptor.DisruptorManager.instance().publishData(disData);
                }
            }
            return bufferFactory.wrap(JsonUtils.getJsonBytes(GlobalMessageResponseVo.newInstance(code, message, errorData)));
        }

    }

    /**
     * 自定义登录表单转换器
     *
     *  ""dministrator
     */
    static class CustomerServerFormLoginAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

        private String usernameParameter = "username";

        private String passwordParameter = "password";

        private String loginSourceParameter = "loginSource";

        //private String validCodeParameter = "code";

        private Integer defaultValidTime = 3;

        private Integer defaultMaxValidTime = 5;

        private ValueOperations<String, String> valOpsStr;

        private AliYunAuthenticateSigConfig aliYunAuthenticateSigConfig;

        public CustomerServerFormLoginAuthenticationConverter(ValueOperations<String, String> valOpsStr, AliYunAuthenticateSigConfig aliYunAuthenticateSigConfig) {
            this.valOpsStr = valOpsStr;
            this.aliYunAuthenticateSigConfig = aliYunAuthenticateSigConfig;
        }

        @Override
        public Mono<Authentication> apply(ServerWebExchange exchange) {
            return exchange.getFormData()
                    .map(data -> createAuthentication(data));
        }

        private UsernamePasswordAuthenticationToken createAuthentication(MultiValueMap<String, String> data) {
            //用户输入的登录名
            String username = data.getFirst(this.usernameParameter);
            //可以解密
            String password = data.getFirst(this.passwordParameter);
            if (StringUtils.isBlank(password)) {
                throw new PlatException(Constants.PARAM_ERROR, "请输入密码");
            }
            if (StringUtils.isBlank(username)) {
                throw new PlatException(Constants.PARAM_ERROR, "请输入用户名");
            }
            password = RsaUtils.decryptByPrivateKey(password, RsaUtils.DEFAULT_PRIVATE_KEY);

            String validTimeKey = new StringBuilder(SercurityConstant.VALID_TIME_USERNAME_REDIS_NAMESAPCE)
                    .append(DateUtil.formatDate()).append(":").append(username).toString();
            Long errorTime = valOpsStr.increment(validTimeKey, 1);
            valOpsStr.getOperations().expire(validTimeKey, SercurityConstant.REDIS_EXPIRE_TIME_ONE_HOUR * 24, TimeUnit.SECONDS);
            if (errorTime > defaultMaxValidTime) {
                //一天登录大于5次不能登录了
                throw new PlatException(Constants.LOGIN_ERROR_MORE_ERROR, "登录错误次数过多");
            }
            if (errorTime > defaultValidTime) {
                String source = data.getFirst("source");
                if (StringUtils.isNotBlank(source) && (source.equalsIgnoreCase("android") || source.equalsIgnoreCase("ios"))) {
                    //阿里滑块验证
                    String appKey = data.getFirst("appKey");
                    String scene = data.getFirst("scene");
                    String sessionId = data.getFirst("sessionId");
                    String otcTag = data.getFirst("authTag");
                    AuthenticateSigVO authenticateSigVO = new AuthenticateSigVO();
                    authenticateSigVO.setAccessKeyId(aliYunAuthenticateSigConfig.getAccessKeyId());
                    authenticateSigVO.setAccessKeySecret(aliYunAuthenticateSigConfig.getAccessKeySecret());
                    if (StringUtils.isNotBlank(otcTag) && "otc".equalsIgnoreCase(otcTag)) {
                        authenticateSigVO.setAccessKeyId(aliYunAuthenticateSigConfig.getOtcAccessKeyId());
                        authenticateSigVO.setAccessKeySecret(aliYunAuthenticateSigConfig.getOtcAccessKeySecret());
                    }
                    authenticateSigVO.setAppKey(appKey);
                    authenticateSigVO.setScene(scene);
                    authenticateSigVO.setSessionId(sessionId);
                    boolean isValid = AliYunAuthenticateSigUtils.isValid(authenticateSigVO);
                    if (!isValid) {
                        throw new PlatException(Constants.VALIDCODE_IS_ERROR, "验证码验证失败");
                    }
                } else {
                    //阿里滑块验证
                    String appKey = data.getFirst("appKey");
                    String scene = data.getFirst("scene");
                    String sessionId = data.getFirst("sessionId");
                    String sig = data.getFirst("sig");
                    String vtoken = data.getFirst("vtoken");
                    AuthenticateSigVO authenticateSigVO = new AuthenticateSigVO();
                    authenticateSigVO.setAccessKeyId(aliYunAuthenticateSigConfig.getAccessKeyId());
                    authenticateSigVO.setAccessKeySecret(aliYunAuthenticateSigConfig.getAccessKeySecret());
                    authenticateSigVO.setAppKey(appKey);
                    authenticateSigVO.setScene(scene);
                    authenticateSigVO.setSessionId(sessionId);
                    authenticateSigVO.setSig(sig);
                    authenticateSigVO.setToken(vtoken);
                    boolean isValid = AliYunAuthenticateSigUtils.isValid(authenticateSigVO);
                    if (!isValid) {
                        throw new PlatException(Constants.VALIDCODE_IS_ERROR, "验证码验证失败");
                    }
                }
            }
            UsernamePasswordAuthenticationTokenLocal tokenLocal = new UsernamePasswordAuthenticationTokenLocal(username, password);
            tokenLocal.setLoginSource(Optional.ofNullable(data.getFirst(this.loginSourceParameter)).orElse("plat"));
            return tokenLocal;
        }

        public void setUsernameParameter(String usernameParameter) {
            Assert.notNull(usernameParameter, "usernameParameter cannot be null");
            this.usernameParameter = usernameParameter;
        }

        public void setPasswordParameter(String passwordParameter) {
            Assert.notNull(passwordParameter, "passwordParameter cannot be null");
            this.passwordParameter = passwordParameter;
        }

        public void setDefaultValidTime(Integer defaultValidTime) {
            this.defaultValidTime = defaultValidTime;
        }

        public void setDefaultMaxValidTime(Integer defaultMaxValidTime) {
            this.defaultMaxValidTime = defaultMaxValidTime;
        }
    }

    /**
     * 自定义登录成功处理
     *
     *  ""dministrator
     */
    static class CustomerServerAuthenticationSuccessHandlerImpl extends CustomerWebFilterChainServerAuthenticationSuccessHandler {

        private ValueOperations<String, String> valOpsStr;
        private MessageSendService messageSendService;
        private SmsMessageService smsMessageService;
        private StringRedisTemplate stringRedisTemplate;
        private boolean isOpendSms = false;

        public CustomerServerAuthenticationSuccessHandlerImpl(ValueOperations<String, String> valOpsStr
                , StringRedisTemplate stringRedisTemplate
                , MessageSendService messageSendService, SmsMessageService smsMessageService) {
            this.valOpsStr = valOpsStr;
            this.stringRedisTemplate = stringRedisTemplate;
            this.messageSendService = messageSendService;
            this.smsMessageService = smsMessageService;
        }

        @Override
        public Mono<Void> onAuthenticationSuccess(Authentication token, WebFilterExchange webFilterExchange,
                                                  Authentication authentication) {
            //清楚错误记录数 token.getName()为用户输入的登录名
            String validTimeKey = new StringBuilder(SercurityConstant.VALID_TIME_USERNAME_REDIS_NAMESAPCE)
                    .append(DateUtil.formatDate()).append(":").append(token.getName()).toString();
            valOpsStr.getOperations().delete(validTimeKey);
            //处理成功登录用户
            ServerWebExchange exchange = webFilterExchange.getExchange();
            ServerHttpResponse result = exchange.getResponse();
            result.setStatusCode(HttpStatus.OK);
            result.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            RedisSessionUser user = (RedisSessionUser) authentication.getPrincipal();
            user.setLoginIp(getIpAddress(exchange));
            String source = ((UsernamePasswordAuthenticationTokenLocal) token).getLoginSource();
            user.setLoginSource(source);
            //失效优化谷歌验证
            String authKey = SercurityConstant.AUTH_USERNAME_REDIS_NAMESAPCE + user.getUsername();
            valOpsStr.getOperations().delete(authKey);
            //异步记录用户登录成功
            SecurityConfig.saveUserLogin(exchange, token.getName(), source, "", user);
            if (isOpendSms) {
                Map<String, Object> datas = new HashMap<>();
                //生成一个临时令牌
                String tempToken = UUID.randomUUID().toString().replaceAll("-", "");
                int type = 0;
                if (StringUtils.isNotBlank(user.getMobile())) {
                    //发送用户短信
                    type = 0;
                    smsMessageService.sendSms(user.getMobile(), MessageTemplateCode.MOBILE_LOGIN_TEMPLATE.getCode(), "");
                } else {
                    //发送用户邮件
                    type = 1;
                    Map<String, Object> params = new HashMap<>();
                    //邮箱注册邮件
                    params.put(MessageSendService.PARAM_CODE, NumberUtils.getRandomNumber(6));
                    messageSendService.mailSend(MessageTemplateCode.LOGIN_TEMPLATE.getCode(), VerificationCodeType.LOGIN_MAIL_CODE, user.getMail(), params);
                }
                valOpsStr.set("temp:" + type + "-" + tempToken, JsonUtils.toJson(user), 120, TimeUnit.SECONDS);
                DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
                datas.put("tempToken", tempToken);
                datas.put("tempTokenType", type);
                Mono<DataBuffer> authenticationUser = Mono.just(bufferFactory.wrap(JsonUtils.getJsonBytes(GlobalMessageResponseVo.newSuccessInstance(datas))));
                return result.writeWith(authenticationUser);
            } else {
                //获取用户的token标识
                String authorizationToken = user.getToken();
                //添加请求头部
                result.getHeaders().add(SercurityConstant.SESSION_TOKEN_HEADER, authorizationToken);
                String userRedisKey = SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + authorizationToken;
                //使用hash结构,存取用户
                stringRedisTemplate.opsForHash().put(userRedisKey, SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                stringRedisTemplate.expire(userRedisKey, SercurityConstant.REDIS_EXPIRE_TIME_SESSION, TimeUnit.SECONDS);

                DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
                user.setPassword(null);
                if (StringUtils.isNotEmpty(user.getExPassword())) {
                    user.setExPassword("true");
                }
                if (StringUtils.isNotEmpty(user.getGoogleAuth())) {
                    user.setGoogleAuth("true");
                }
                Mono<DataBuffer> authenticationUser = Mono.just(bufferFactory.wrap(JsonUtils.getJsonBytes(GlobalMessageResponseVo.newSuccessInstance(user))));
                // 发送消息
                //producer.send("topic.user.login.message", user.getUsername(), user);
                return result.writeWith(authenticationUser);
            }
        }

        public void setOpendSms(boolean isOpendSms) {
            this.isOpendSms = isOpendSms;
        }

    }

    public static String getIpAddress(ServerWebExchange exchange) {
        return getRemoteAddr(exchange.getRequest());
    }

    public static String getRemoteAddr(ServerHttpRequest request) {
        String remoteAddr = request.getHeaders().getFirst("X-Forwarded-For");
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeaders().getFirst("X-Real-IP");
        }
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddress().getAddress().getHostAddress();
    }

    /**
     * 自定义401,用户没有登录访问需要登录的资源
     *
     *  ""dministrator
     */
    static class CustomerServerAuthenticationEntryPoint extends HttpBasicServerAuthenticationEntryPoint {

        private static Logger logger = LoggerFactory.getLogger(CustomerServerAuthenticationEntryPoint.class);

        @Override
        public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("访问需要授权的页面,需要登录,url = {},e = {}", exchange.getRequest().getURI(), e.getMessage());
            }
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            DataBufferFactory bufferFactory = response.bufferFactory();
            return response.writeWith(Mono.just(bufferFactory.wrap(JsonUtils.getJsonBytes(GlobalMessageResponseVo.newInstance(Constants.NEED_LOGIN_CODE, "请先登录")))));

        }

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    public static void saveUserLogin(ServerWebExchange exchange, String username, String source, String remark, RedisSessionUser user) {
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setRemark(remark);
        userLoginLog.setIp(getIpAddress(exchange));
        userLoginLog.setLoginName(username);
        userLoginLog.setSource(source);
        userLoginLog.setLoginTime(LocalDateTime.now());
        if (user != null) {
            userLoginLog.setUserId(user.getId());
            userLoginLog.setMail(user.getMail());
            userLoginLog.setMobile(user.getMobile());
            userLoginLog.setStatus(0);
        } else {
            userLoginLog.setStatus(1);
        }
        DisruptorManager.instance().runConfig();
        DisruptorData data = new DisruptorData();
        data.setType(5);
        data.setUserLoginLog(userLoginLog);
        ;
        DisruptorManager.instance().publishData(data);
    }
}
