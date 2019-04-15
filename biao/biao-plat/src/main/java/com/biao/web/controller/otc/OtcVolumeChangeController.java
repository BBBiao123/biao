package com.biao.web.controller.otc;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcVolumeChangeService;
import com.biao.vo.otc.OtcVolumeChangeQueryVO;
import com.biao.vo.otc.OtcVolumeChangeRequestVO;
import com.biao.vo.otc.OtcVolumeChangeResultVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 资产变更
 *
 *  ""zj
 */
@RestController
@RequestMapping("/biao")
public class OtcVolumeChangeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtcVolumeChangeController.class);

    @Autowired
    private OtcVolumeChangeService otcVolumeChangeService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String OTC_VOLUME_CACHE_KEY = "otc:volume:change:%s";

    private static final int TIME_OUT = 60; // 缓存5分钟

    /**
     * OTC资产变更
     * @return
     */
    @PostMapping("/otc/volume/change")
    public Mono<GlobalMessageResponseVo> change(OtcVolumeChangeRequestVO otcVolumeChangeRequestVO) {

        Mono<SecurityContext> context  = ReactiveSecurityContextHolder.getContext();
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    otcVolumeChangeRequestVO.setLoginUserId(e.getId());
                    otcVolumeChangeRequestVO.setRequestUri("/otc/volume/change");
                    return GlobalMessageResponseVo.newSuccessInstance(otcVolumeChangeService.change(otcVolumeChangeRequestVO));
                });
    }

    /**
     * OTC资产变更(申诉)
     * @return
     */
    @PostMapping("/otc/volume/admin/change")
    public Mono<GlobalMessageResponseVo> changeForAdmin(OtcVolumeChangeRequestVO otcVolumeChangeRequestVO) {

        otcVolumeChangeRequestVO.setRequestUri("/otc/volume/admin/change");
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcVolumeChangeService.change(otcVolumeChangeRequestVO)));

//        Mono<SecurityContext> context  = ReactiveSecurityContextHolder.getContext();
//        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
//                .map(s -> s.getAuthentication().getPrincipal())
//                .cast(RedisSessionUser.class)
//                .map(e -> {
//                    otcVolumeChangeRequestVO.setLoginUserId(e.getId());
//                    otcVolumeChangeRequestVO.setRequestUri("/otc/volume/admin/change");
//                    return GlobalMessageResponseVo.newSuccessInstance(otcVolumeChangeService.change(otcVolumeChangeRequestVO));
//                });
    }

    /**
     * OTC资产变更查询
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "batchNo", notNull = true, errMsg = "批次流水号不能为空"),
            @ValidateFiled(index = 0, filedName = "publishSource", notNull = true, errMsg = "来源不能为空"),
            @ValidateFiled(index = 0, filedName = "key", notNull = true, errMsg = "密文不能为空"),
    })
    @GetMapping("/otc/volume/query")
    public Mono<GlobalMessageResponseVo> findByBatch(OtcVolumeChangeQueryVO otcVolumeChangeQueryVO) {

        String cacheKey = String.format(OTC_VOLUME_CACHE_KEY, otcVolumeChangeQueryVO.getBatchNo());
        OtcVolumeChangeResultVO resultVO = (OtcVolumeChangeResultVO) redisTemplate.opsForValue().get(cacheKey);
        if (Objects.isNull(resultVO)) {
            resultVO = otcVolumeChangeService.findByBatchNo(otcVolumeChangeQueryVO);
            if (Objects.nonNull(resultVO)) {
                redisTemplate.opsForValue().set(cacheKey, resultVO, TIME_OUT, TimeUnit.SECONDS);
            }
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(resultVO));
    }

}
