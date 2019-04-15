package com.biao.web.controller.otc;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.otc.OtcConvertCoin;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcConvertCoinService;
import com.biao.utils.ServerWebExchangeUtils;
import com.biao.vo.otc.OtcConvertCoinVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 币种兑换
 *
 *  ""f
 */
@RestController
@RequestMapping("/biao")
public class OtcConvertCoinController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtcVolumeChangeController.class);

    @Autowired
    private OtcConvertCoinService otcConvertCoinService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String OTC_CONVERT_CACHE_KEY = "otc:convert:coin:%s";

    private static final int TIME_OUT = 60; // 缓存5分钟

    /**
     * 查询币种兑换结果
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "批次流水号不能为空"),
            @ValidateFiled(index = 0, filedName = "publishSource", notNull = true, errMsg = "来源不能为空"),
            @ValidateFiled(index = 0, filedName = "key", notNull = true, errMsg = "密文不能为空"),
    })
    @GetMapping("/otc/coin/convert/query")
    public Mono<GlobalMessageResponseVo> findByBatchNo(OtcConvertCoinVO otcConvertCoinVO) {

        String cacheKey = String.format(OTC_CONVERT_CACHE_KEY, otcConvertCoinVO.getBatchNo());
        OtcConvertCoin resultVO = (OtcConvertCoin) redisTemplate.opsForValue().get(cacheKey);
        if (Objects.isNull(resultVO)) {
            resultVO = otcConvertCoinService.findByBatchNo(otcConvertCoinVO);
            if (Objects.nonNull(resultVO)) {
                redisTemplate.opsForValue().set(cacheKey, resultVO, TIME_OUT, TimeUnit.SECONDS);
            }
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(resultVO));
    }

    /**
     * 币种兑换
     * @return
     */
    @PostMapping("/otc/coin/convert")
    public Mono<GlobalMessageResponseVo> execute(OtcConvertCoinVO otcConvertCoinVO, ServerWebExchange exchange) {
        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String remoteAddr = ServerWebExchangeUtils.getRemoteAddr(exchange);
                    otcConvertCoinVO.setLoginIp(remoteAddr);
                    OtcConvertCoin otcConvertCoin = new OtcConvertCoin();
                    BeanUtils.copyProperties(otcConvertCoinVO, otcConvertCoin);
                    otcConvertCoin.setUserId(e.getId());
                    return GlobalMessageResponseVo.newSuccessInstance(otcConvertCoinService.executeConvert(otcConvertCoin, otcConvertCoinVO)); // 调用支付接口
                });
    }
}
