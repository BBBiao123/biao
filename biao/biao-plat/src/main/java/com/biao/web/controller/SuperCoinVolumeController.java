package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.SuperCoinVolume;
import com.biao.entity.SuperCoinVolumeConf;
import com.biao.mapper.SuperCoinVolumeConfDao;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.CoinService;
import com.biao.service.SuperCoinVolumeService;
import com.biao.vo.SuperTransferVO;
import com.biao.vo.SuperVolumeVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 超级账本用户资产
 *
 *  ""azi
 */
@RestController
@RequestMapping("/biao")
public class SuperCoinVolumeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuperCoinVolumeController.class);

    @Autowired
    private SuperCoinVolumeService superCoinVolumeService;

    @Autowired
    private SuperCoinVolumeConfDao superCoinVolumeConfDao;

    @Autowired
    private CoinService coinService;

    @GetMapping("/super/volume/list")
    public Mono<GlobalMessageResponseVo> findAll() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();



        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {

                    SuperCoinVolumeConf superCoinVolumeConf = superCoinVolumeConfDao.findOne();
                    if(Objects.isNull(superCoinVolumeConf)){
                        return GlobalMessageResponseVo.newErrorInstance("数据异常！");
                    }

                    List<SuperVolumeVO> listVo = new ArrayList<>();
                    List<SuperCoinVolume> listVolume = superCoinVolumeService.findAll(e.getId());
                    if (CollectionUtils.isNotEmpty(listVolume)) {
                        listVolume.forEach(superCoinVolume -> {
                            if (superCoinVolume != null) {
                                SuperVolumeVO superVolumeVO = new SuperVolumeVO();
                                BeanUtils.copyProperties(superCoinVolume, superVolumeVO);
                                superVolumeVO.setBreakRatio(superCoinVolumeConf.getBreakRatio());
                                superVolumeVO.setMultiple(superCoinVolumeConf.getMultiple());
                                superVolumeVO.setRemainingDays(Long.valueOf(superCoinVolumeConf.getLockCycle()));
                                superVolumeVO.setFrozenDays(Long.valueOf(superCoinVolumeConf.getFrozenDay()));
                                superCoinVolume.setVolume(superCoinVolume.getVolume().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                                listVo.add(superVolumeVO);
                            }
                        });

                    }else{
                        SuperVolumeVO superVolumeVO = new SuperVolumeVO();
                        superVolumeVO.setCoinId(superCoinVolumeConf.getCoinId());
                        superVolumeVO.setCoinSymbol(superCoinVolumeConf.getCoinSymbol());
                        superVolumeVO.setBreakRatio(superCoinVolumeConf.getBreakRatio());
                        superVolumeVO.setMultiple(superCoinVolumeConf.getMultiple());
                        superVolumeVO.setRemainingDays(Long.valueOf(superCoinVolumeConf.getLockCycle()));
                        superVolumeVO.setFrozenDays(Long.valueOf(superCoinVolumeConf.getFrozenDay()));
                        listVo.add(superVolumeVO);
                    }
                    return GlobalMessageResponseVo
                            .newSuccessInstance(listVo);
                });
    }

    /**
     * 资产转入
     *
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "币种符号不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "币种数量不能为空"),
    })
    @PostMapping("/super/volume/in")
    public Mono<GlobalMessageResponseVo> in(SuperTransferVO superTransferVO) {
        if (superTransferVO.getVolume().compareTo(BigDecimal.ZERO) == -1) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("转入数量非法"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    LOGGER.info("用户超级账本划入参数：" + "volume:" + superTransferVO.getVolume() + " source:" + user.getLoginSource());
                    //确保coinId与coinSymbol一致
                    Coin coin = coinService.findById(superTransferVO.getCoinId());
                    if(Objects.isNull(coin)){
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.VOLUME_IN_OUT_ERROR, "参数异常"));
                    }
                    superCoinVolumeService.in(user.getId(), superTransferVO.getCoinId(), superTransferVO.getVolume(), coin.getName());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("转入成功"));
                });
    }

    /**
     * 资产转出
     *
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "币种符号不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "币种数量不能为空"),
    })
    @PostMapping("/super/volume/out")
    public Mono<GlobalMessageResponseVo> out(SuperTransferVO superTransferVO) {
        if (superTransferVO.getVolume().compareTo(BigDecimal.ZERO) == -1) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("转入数量非法"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    LOGGER.info("用户超级账本转出参数：" + "volume:" + superTransferVO.getVolume() +" source:" + user.getLoginSource());

                    //确保coinId与coinSymbol一致
                    Coin coin = coinService.findById(superTransferVO.getCoinId());
                    if(Objects.isNull(coin)){
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.VOLUME_IN_OUT_ERROR, "参数异常"));
                    }

                    superCoinVolumeService.out(user.getId(), superTransferVO.getCoinId(), superTransferVO.getVolume(), coin.getName());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("转出成功"));
                });
    }

//    /**
//     * 获取超级账本资产余额
//     *
//     * @return
//     */
//
//    @GetMapping("/super/volume/{coinId}")
//    public Mono<GlobalMessageResponseVo> offlineVolume(@PathVariable("coinId") String coinId) {
//        return ReactiveSecurityContextHolder.getContext()
//                .filter(c -> c.getAuthentication() != null)
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(RedisSessionUser.class)
//                .flatMap(user -> Mono
//                        .just(GlobalMessageResponseVo.newSuccessInstance(superCoinVolumeService.findByUserIdAndCoinId(user.getId(), coinId)
//                        )));
//    }

}

