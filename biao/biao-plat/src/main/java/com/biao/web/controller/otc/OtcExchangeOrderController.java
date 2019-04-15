package com.biao.web.controller.otc;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.otc.OtcExchangeOrder;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcAccountSecretService;
import com.biao.service.otc.OtcExchangeOrderService;
import com.biao.utils.ServerWebExchangeUtils;
import com.biao.vo.otc.OtcExchangeOrderVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * OTC商家支付
 *
 *  ""ong
 */
@RestController
@RequestMapping("/biao")
public class OtcExchangeOrderController {

    @Autowired
    private OtcExchangeOrderService otcExchangeOrderService;

    @Autowired
    private OtcAccountSecretService otcAccountSecretService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String OTC_EXCHANGE_CACHE_KEY = "otc:exchange:%s";

    private static final int TIME_OUT = 60; // 缓存1分钟

    /**
     * 根据流水号获取通兑状态 免登陆
     *
     * @param otcExchangeOrderVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "通兑流水号不能为空"),
    })
    @GetMapping("/otc/exchange/find")
    public Mono<GlobalMessageResponseVo> findExchange(OtcExchangeOrderVO otcExchangeOrderVO) {

//        return ReactiveSecurityContextHolder.getContext()
//                .filter(c -> c.getAuthentication() != null)
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(RedisSessionUser.class).flatMap(user -> {
//                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
                    String cacheKey = String.format(OTC_EXCHANGE_CACHE_KEY, otcExchangeOrderVO.getBatchNo());
                    OtcExchangeOrder otcExchangeOrder = (OtcExchangeOrder) redisTemplate.opsForValue().get(cacheKey);
                    if (Objects.isNull(otcExchangeOrder)) {
                        otcExchangeOrder = otcExchangeOrderService.findByBatchNo(otcExchangeOrderVO.getBatchNo());
                        if (Objects.nonNull(otcExchangeOrder)) {
                            redisTemplate.opsForValue().set(cacheKey, otcExchangeOrder, TIME_OUT, TimeUnit.SECONDS);
                        }
                    }
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcExchangeOrder));
//                });
    }

//    /**
//     * 通兑支付接口
//     *
//     * @return
//     */
//    @ValidateGroup(fileds = {
//            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "通兑流水号不能为空"),
//            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "支付币种不能为空"),
//            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "通兑数量不能为空"),
//            @ValidateFiled(index = 0, filedName = "askUserId", notNull = true, errMsg = "收款方ID不能为空"),
//    })
//    @PostMapping("/otc/exchange/pay")
//    public Mono<GlobalMessageResponseVo> pay(OtcExchangeOrderVO otcExchangeOrderVO) {
//        Mono<SecurityContext> context
//                = ReactiveSecurityContextHolder.getContext();
//        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
//                .map(s -> s.getAuthentication().getPrincipal())
//                .cast(RedisSessionUser.class)
//                .map(e -> {
//
//                    OtcExchangeOrder otcExchangeOrder = new OtcExchangeOrder();
//                    BeanUtils.copyProperties(otcExchangeOrderVO, otcExchangeOrder);
//                    otcExchangeOrder.setUserId(e.getId());
//                    otcExchangeOrder.setRealName(e.getRealName());
//                    otcExchangeOrder.setMobile(e.getMobile());
//                    otcExchangeOrder.setPublishSource(e.getLoginSource());
//                    return GlobalMessageResponseVo.newSuccessInstance(otcExchangeOrderService.userPay(otcExchangeOrder)); // 调用支付接口
//                });
//    }
//
//    /**
//     * 通兑支付接口，免登录
//     *
//     * @return
//     */
//    @ValidateGroup(fileds = {
//            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "通兑流水号不能为空"),
//            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "支付币种不能为空"),
//            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "通兑数量不能为空"),
//            @ValidateFiled(index = 0, filedName = "askUserId", notNull = true, errMsg = "收款方ID不能为空"),
//            @ValidateFiled(index = 0, filedName = "userId", notNull = true, errMsg = "支款方ID不能为空"),
//            @ValidateFiled(index = 0, filedName = "key", notNull = true, errMsg = "支付秘钥不能为空"),
//    })
//    @PostMapping("/otc/exchange/paynologin")
//    public Mono<GlobalMessageResponseVo> payNoLogin(OtcExchangeOrderVO otcExchangeOrderVO, ServerWebExchange exchange) {
//
////        return ReactiveSecurityContextHolder.getContext()
////                .filter(c -> c.getAuthentication() != null)
////                .map(SecurityContext::getAuthentication)
////                .map(Authentication::getPrincipal)
////                .cast(RedisSessionUser.class).flatMap(user -> {
////                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
//                    Map<String, String> paramMap = new HashMap<>();
//                    paramMap.put("batchNo", otcExchangeOrderVO.getBatchNo());
//                    paramMap.put("symbol", otcExchangeOrderVO.getSymbol());
//                    paramMap.put("volume", String.valueOf(otcExchangeOrderVO.getVolume()));
//                    paramMap.put("askUserId", otcExchangeOrderVO.getAskUserId());
//                    paramMap.put("userId", otcExchangeOrderVO.getUserId());
//                    // 获取IP
//                    String remoteAddr = ServerWebExchangeUtils.getRemoteAddr(exchange);
//                    otcExchangeOrderVO.setLoginIp(remoteAddr);
//                    //if(null == otcExchangeOrderVO.getPublishSource()){
//                    otcExchangeOrderVO.setPublishSource("otc");
//                    //}
//
//                    OtcExchangeOrder otcExchangeOrder = new OtcExchangeOrder();
//                    BeanUtils.copyProperties(otcExchangeOrderVO, otcExchangeOrder);
//                    //otcExchangeOrder.setPublishSource("otc");
//                    otcExchangeOrder.setCreateBy("nologin");
//                    otcExchangeOrder.setIp(remoteAddr);
//
//                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcExchangeOrderService.otcPay(otcExchangeOrder, paramMap, otcExchangeOrderVO)));// 调用支付接口
////                });
//    }

    /**
     * 通兑支付接口
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "通兑流水号不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "支付币种不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "通兑数量不能为空"),
            @ValidateFiled(index = 0, filedName = "askUserId", notNull = true, errMsg = "收款方ID不能为空"),
            @ValidateFiled(index = 0, filedName = "askVolume", notNull = true, errMsg = "收款方数量不能为空"),
    })
    @PostMapping("/otc/exchange/cust/pay")
    public Mono<GlobalMessageResponseVo> custPay(OtcExchangeOrderVO otcExchangeOrderVO) {
        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {

                    OtcExchangeOrder otcExchangeOrder = new OtcExchangeOrder();
                    BeanUtils.copyProperties(otcExchangeOrderVO, otcExchangeOrder);
                    otcExchangeOrder.setUserId(e.getId());
                    otcExchangeOrder.setRealName(e.getRealName());
                    otcExchangeOrder.setMobile(e.getMobile());
                    otcExchangeOrder.setPublishSource(e.getLoginSource());
                    otcExchangeOrder.setRealVolume(otcExchangeOrderVO.getAskVolume());
                    return GlobalMessageResponseVo.newSuccessInstance(otcExchangeOrderService.custUserPay(otcExchangeOrder)); // 调用支付接口
                });
    }

    /**
     * 通兑支付接口，免登录
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "通兑流水号不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "支付币种不能为空"),
            @ValidateFiled(index = 0, filedName = "userId", notNull = true, errMsg = "付款方ID不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "付款数量不能为空"),
            @ValidateFiled(index = 0, filedName = "askUserId", notNull = true, errMsg = "收款方ID不能为空"),
            @ValidateFiled(index = 0, filedName = "askVolume", notNull = true, errMsg = "收款方数量不能为空"),
    })
    @PostMapping("/otc/exchange/cust/paynologin")
    public Mono<GlobalMessageResponseVo> custPayNoLogin(OtcExchangeOrderVO otcExchangeOrderVO, ServerWebExchange exchange) {

//        return ReactiveSecurityContextHolder.getContext()
//                .filter(c -> c.getAuthentication() != null)
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(RedisSessionUser.class).flatMap(user -> {
//                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("batchNo", otcExchangeOrderVO.getBatchNo());
                    paramMap.put("symbol", otcExchangeOrderVO.getSymbol());
                    paramMap.put("volume", String.valueOf(otcExchangeOrderVO.getVolume()));
                    paramMap.put("askUserId", otcExchangeOrderVO.getAskUserId());
                    paramMap.put("userId", otcExchangeOrderVO.getUserId());
                    paramMap.put("askVolume", String.valueOf(otcExchangeOrderVO.getAskVolume()));
                    // 获取IP
                    String remoteAddr = ServerWebExchangeUtils.getRemoteAddr(exchange);
                    otcExchangeOrderVO.setLoginIp(remoteAddr);
                    //if(null == otcExchangeOrderVO.getPublishSource()){
                    otcExchangeOrderVO.setPublishSource("otc");
                    //}

                    OtcExchangeOrder otcExchangeOrder = new OtcExchangeOrder();
                    BeanUtils.copyProperties(otcExchangeOrderVO, otcExchangeOrder);
                    //otcExchangeOrder.setPublishSource("otc");
                    otcExchangeOrder.setCreateBy("nologin");
                    otcExchangeOrder.setIp(remoteAddr);
                    otcExchangeOrder.setRealVolume(otcExchangeOrderVO.getAskVolume());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcExchangeOrderService.custOtcPay(otcExchangeOrder, paramMap, otcExchangeOrderVO)));// 调用支付接口
//                });

    }

    /**
     * 获取通兑流水号 免登陆
     *
     * @return
     */
    @GetMapping("/otc/exchange/getno")
    public Mono<GlobalMessageResponseVo> getBatchNo() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcExchangeOrderService.getBatchNo()));// 获取批次号
    }
}
