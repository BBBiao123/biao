package com.biao.web.controller.otc;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.entity.otc.OtcAppropriationRequest;
import com.biao.entity.otc.OtcOfflineAppealRequest;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcAccountSecretService;
import com.biao.service.otc.OtcAdminService;
import com.biao.utils.ServerWebExchangeUtils;
import com.biao.vo.otc.OtcAppropriationRequestVO;
import com.biao.vo.otc.OtcCoinVO;
import com.biao.vo.otc.OtcOfflineAppealRequestVO;
import com.biao.vo.otc.OtcOfflineOrderDetailVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
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
 * OTC管理员后台
 *
 *  ""ong
 */
@RestController
@RequestMapping("/biao")
public class OtcAdminController {

    @Autowired
    private OtcAdminService otcAdminService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String OTC_APPEAL_CACHE_KEY = "otc:appeal:%s";

    private static final String OTC_APPROPRIATION_CACHE_KEY = "otc:appropriation:%s";// Appropriation

    private static final int TIME_OUT = 60; // 缓存1分钟

    @Autowired
    private OtcAccountSecretService otcAccountSecretService;

    /**
     * 二次校验确认接口
     *
     * @return
     */
    @PostMapping("/otc/secendCheck")
    public Mono<GlobalMessageResponseVo> secendCheck() {

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                     return Mono.just(GlobalMessageResponseVo.newInstance(Constants.SUCCESS_CODE, "ok"));
                });
    }

    /**
     * 获取币币最新汇率--目前支持EUC-UES
     *
     * @return
     */
    @GetMapping("/otc/getLastRate")
    public Mono<GlobalMessageResponseVo> getLastRate() {
        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.SUCCESS_CODE, "ok", otcAdminService.getLastRate("EUC", "UES")));
    }

    /**
     * 获取币种ID  免登陆
     *
     * @param otcCoinVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "name", notNull = true, errMsg = "币种代码不能为空"),
    })
    @GetMapping("/otc/admin/coin")
    public Mono<GlobalMessageResponseVo> findCoin(OtcCoinVO otcCoinVO) {

//        return ReactiveSecurityContextHolder.getContext()
//                .filter(c -> c.getAuthentication() != null)
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(RedisSessionUser.class).flatMap(user -> {
//                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("name", otcCoinVO.getName());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAdminService.findCoin(otcCoinVO, paramMap)));
//                });
    }

    @GetMapping("/otc/admin/batchno")
    public Mono<GlobalMessageResponseVo> getBatchNo() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAdminService.getBatchNo()));
    }

    /**
     * 申诉批次号查询 免登陆
     *
     * @param otcOfflineAppealRequestVO
     * @return
     */
//    @ValidateGroup(fileds = {
//            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "批次流水号不能为空"),
//    })
//    @PostMapping("/otc/admin/appeal/find")
//    public Mono<GlobalMessageResponseVo> appealFind(OtcOfflineAppealRequestVO otcOfflineAppealRequestVO) {
//
////        return ReactiveSecurityContextHolder.getContext()
////                .filter(c -> c.getAuthentication() != null)
////                .map(SecurityContext::getAuthentication)
////                .map(Authentication::getPrincipal)
////                .cast(RedisSessionUser.class).flatMap(user -> {
////                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
//                    String cacheKey = String.format(OTC_APPEAL_CACHE_KEY, otcOfflineAppealRequestVO.getBatchNo());
//                    OtcOfflineAppealRequest otcOfflineAppealRequest = (OtcOfflineAppealRequest) redisTemplate.opsForValue().get(cacheKey);
//                    if (Objects.isNull(otcOfflineAppealRequest)) {
//                        otcOfflineAppealRequest = otcAdminService.findAppeal(otcOfflineAppealRequestVO);
//                        if (Objects.nonNull(otcOfflineAppealRequest)) {
//                            redisTemplate.opsForValue().set(cacheKey, otcOfflineAppealRequest, TIME_OUT, TimeUnit.SECONDS);
//                        }
//                    }
//                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcOfflineAppealRequest));
////                });
//
//
//    }

    /**
     * 申诉处理 免登陆
     *
     * @param otcOfflineAppealRequestVO
     * @return
     */
//    @ValidateGroup(fileds = {
//            @ValidateFiled(index = 0, filedName = "publishSource", notNull = true, errMsg = "来源不能为空"),
//            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "批次号不能为空"),
//            @ValidateFiled(index = 0, filedName = "subOrderId", notNull = true, errMsg = "订单号不能为空"),
//            @ValidateFiled(index = 0, filedName = "resultUserId", notNull = true, errMsg = "胜方ID不能为空"),
//            @ValidateFiled(index = 0, filedName = "resultExType", notNull = true, errMsg = "买卖方向不能为空"),
//    })
//    @PostMapping("/otc/admin/appeal")
//    public Mono<GlobalMessageResponseVo> doAppeal(OtcOfflineAppealRequestVO otcOfflineAppealRequestVO, ServerWebExchange exchange) {
//
////        return ReactiveSecurityContextHolder.getContext()
////                .filter(c -> c.getAuthentication() != null)
////                .map(SecurityContext::getAuthentication)
////                .map(Authentication::getPrincipal)
////                .cast(RedisSessionUser.class).flatMap(user -> {
////                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
//                    Map<String, String> paramMap = new HashMap<>();
//                    paramMap.put("publishSource", otcOfflineAppealRequestVO.getPublishSource());
//                    paramMap.put("batchNo", otcOfflineAppealRequestVO.getBatchNo());
//                    paramMap.put("subOrderId", otcOfflineAppealRequestVO.getSubOrderId());
//                    paramMap.put("resultUserId", otcOfflineAppealRequestVO.getResultUserId());
//                    paramMap.put("resultExType", otcOfflineAppealRequestVO.getResultExType());
//
//                    // 获取IP
//                    String remoteAddr = ServerWebExchangeUtils.getRemoteAddr(exchange);
//                    otcOfflineAppealRequestVO.setLoginIp(remoteAddr);
//                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAdminService.doAppeal(otcOfflineAppealRequestVO, paramMap)));
////                });
//    }

//    /**
//     * 申诉处理
//     * @param otcOfflineAppealRequestVO
//     * @return
//     */
//    @ValidateGroup(fileds = {
//            @ValidateFiled(index = 0, filedName = "secretKey", notNull = true, errMsg = "参数不能为空"),
//    })
//    @PostMapping("/otc/admin/appeal")
//    public Mono<GlobalMessageResponseVo> doAppeal(OtcOfflineAppealRequestVO otcOfflineAppealRequestVO) {
//        Map<String, String> paramMap = RsaOtcUtils.getParamMapBySecretKey(otcOfflineAppealRequestVO.getSecretKey());
//        OtcOfflineAppealRequestVO requestVO = RsaOtcUtils.getParamObject(paramMap, OtcOfflineAppealRequestVO.class);
//        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAdminService.doAppeal(requestVO, paramMap)));
//    }

    /**
     * 拨币按批次号查询  OTC主账户拨往银商账户  免登陆
     *
     * @param otcAppropriationRequestVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "批次流水号不能为空"),
    })
    @PostMapping("/otc/admin/appropriation/find")
    public Mono<GlobalMessageResponseVo> appropriationFind(OtcAppropriationRequestVO otcAppropriationRequestVO) {

//        return ReactiveSecurityContextHolder.getContext()
//                .filter(c -> c.getAuthentication() != null)
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(RedisSessionUser.class).flatMap(user -> {
//                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
                    String cacheKey = String.format(OTC_APPROPRIATION_CACHE_KEY, otcAppropriationRequestVO.getBatchNo());
                    OtcAppropriationRequest otcAppropriationRequest = (OtcAppropriationRequest) redisTemplate.opsForValue().get(cacheKey);
                    if (Objects.isNull(otcAppropriationRequest)) {
                        otcAppropriationRequest = otcAdminService.findAppropriation(otcAppropriationRequestVO);
                        if (Objects.nonNull(otcAppropriationRequest)) {
                            redisTemplate.opsForValue().set(cacheKey, otcAppropriationRequest, TIME_OUT, TimeUnit.SECONDS);
                        }
                    }
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAppropriationRequest));
//                });
    }


    /**
     * 拨币  OTC主账户拨往银商账户  免登陆
     *
     * @param otcAppropriationRequestVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "publishSource", notNull = true, errMsg = "来源不能为空"),
            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "批次号不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "币种代码不能为空"),
            @ValidateFiled(index = 0, filedName = "userIdStr", notNull = true, errMsg = "用户ID不能为空"),
            @ValidateFiled(index = 0, filedName = "volumeStr", notNull = true, errMsg = "拨币数量不能为空"),
    })
    @PostMapping("/otc/admin/appropriation")
    public Mono<GlobalMessageResponseVo> doAppropriation(OtcAppropriationRequestVO otcAppropriationRequestVO, ServerWebExchange exchange) {

//        return ReactiveSecurityContextHolder.getContext()
//                .filter(c -> c.getAuthentication() != null)
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(RedisSessionUser.class).flatMap(user -> {
//                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("publishSource", otcAppropriationRequestVO.getPublishSource());
                    paramMap.put("batchNo", otcAppropriationRequestVO.getBatchNo());
                    paramMap.put("symbol", otcAppropriationRequestVO.getSymbol());
                    paramMap.put("userIdStr", otcAppropriationRequestVO.getUserIdStr());
                    paramMap.put("volumeStr", otcAppropriationRequestVO.getVolumeStr());

                    // 获取IP
                    String remoteAddr = ServerWebExchangeUtils.getRemoteAddr(exchange);
                    otcAppropriationRequestVO.setLoginIp(remoteAddr);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAdminService.doAppropriation(otcAppropriationRequestVO, paramMap)));
//                });
    }

//    /**
//     * 拨币  OTC主账户拨往银商账户
//     * @param otcAppropriationRequestVO
//     * @return
//     */
//    @ValidateGroup(fileds = {
//            @ValidateFiled(index = 0, filedName = "secretKey", notNull = true, errMsg = "参数不能为空"),
//    })
//    @PostMapping("/otc/admin/appropriation")
//    public Mono<GlobalMessageResponseVo> doAppropriation(OtcAppropriationRequestVO otcAppropriationRequestVO) {
//        Map<String, String> paramMap = RsaOtcUtils.getParamMapBySecretKey(otcAppropriationRequestVO.getSecretKey());
//        OtcAppropriationRequestVO requestVO = RsaOtcUtils.getParamObject(paramMap, OtcAppropriationRequestVO.class);
//        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAdminService.doAppropriation(requestVO, paramMap)));
//    }

    /**
     * 查询订单
     *
     * @param otcOfflineOrderDetailVO
     * @param exchange
     * @return
     */
//    @ValidateGroup(fileds = {
//            @ValidateFiled(index = 0, filedName = "ts", notNull = true, errMsg = "时间戳不能为空")
//    })
//    @PostMapping("/otc/admin/detail/find")
//    public Mono<GlobalMessageResponseVo> getDetails(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO, ServerWebExchange exchange) {
//
////        return ReactiveSecurityContextHolder.getContext()
////                .filter(c -> c.getAuthentication() != null)
////                .map(SecurityContext::getAuthentication)
////                .map(Authentication::getPrincipal)
////                .cast(RedisSessionUser.class).flatMap(user -> {
////                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
//                    Map<String, String> paramMap = new HashMap<>();
//                    paramMap.put("userMobile", otcOfflineOrderDetailVO.getUserMobile());
//                    paramMap.put("symbol", otcOfflineOrderDetailVO.getSymbol());
//                    paramMap.put("orderId", otcOfflineOrderDetailVO.getOrderId());
//                    paramMap.put("subOrderId", otcOfflineOrderDetailVO.getSubOrderId());
//                    paramMap.put("exType", otcOfflineOrderDetailVO.getExType());
//                    paramMap.put("status", otcOfflineOrderDetailVO.getStatus());
//                    paramMap.put("ts", otcOfflineOrderDetailVO.getTs());
//
//                    otcOfflineOrderDetailVO.setPublishSource("otc");
//                    // 获取IP
//                    String remoteAddr = ServerWebExchangeUtils.getRemoteAddr(exchange);
//                    otcOfflineOrderDetailVO.setLoginIp(remoteAddr);
//
//                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAdminService.getDetails(otcOfflineOrderDetailVO, paramMap)));
////                });
//    }

}
