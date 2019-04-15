package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.OfflineCoin;
import com.biao.entity.OfflineCoinVolume;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.CoinService;
import com.biao.service.OfflineCoinService;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.OfflineTransferLogService;
import com.biao.vo.OfflineTransferListVO;
import com.biao.vo.OfflineTransferVO;
import com.biao.vo.OfflineVolumeVO;
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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * c2c用户资产
 *
 *  ""azi
 */
@RestController
@RequestMapping("/biao")
public class OfflineUserCoinVolumeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfflineUserCoinVolumeController.class);

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;
    @Autowired
    private OfflineCoinService offlineCoinService;

    @Autowired
    private OfflineTransferLogService offlineTransferLogService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/offline/volume/list")
    public Mono<GlobalMessageResponseVo> findAll() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    //查找c2c所有币种
                    List<OfflineCoin> list = offlineCoinService.findAccountAll();
                    List<OfflineVolumeVO> listVo = new ArrayList<>(5);
                    list.forEach(offlineCoin -> {
                        OfflineVolumeVO offlineVolumeVO = new OfflineVolumeVO();
                        BeanUtils.copyProperties(offlineCoin, offlineVolumeVO);
                        listVo.add(offlineVolumeVO);
                    });
                    // BeanUtils.copyProperties(list,listVo,List.class);
                    List<OfflineCoinVolume> listVolume = offlineCoinVolumeService.findAll(e.getId());
                    if (CollectionUtils.isNotEmpty(listVolume)) {
                        listVo.forEach(offlineVolumeVO -> {
                            listVolume.forEach(offlineCoinVolume -> {
                                if (offlineCoinVolume != null) {
                                    if (offlineCoinVolume.getCoinId().equals(offlineVolumeVO.getCoinId())) {
                                        BeanUtils.copyProperties(offlineCoinVolume, offlineVolumeVO);
                                    }
                                }
                            });
                        });
                    }
                    return GlobalMessageResponseVo
                            .newSuccessInstance(listVo);
                });
    }

    /**
     * 资产划转
     *
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "币种符号不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "币种数量不能为空"),
            @ValidateFiled(index = 0, filedName = "from", notNull = true, errMsg = "from不能为空"),
            @ValidateFiled(index = 0, filedName = "to", notNull = true, errMsg = "to不能为空"),
    })
    @PostMapping("/offline/volume/inOut")
    public Mono<GlobalMessageResponseVo> inOut(OfflineTransferVO offlineTransferVO) {
        if (offlineTransferVO.getVolume().compareTo(BigDecimal.ZERO) == -1) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("转入数量非法"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    LOGGER.info("用户划转参数：" + "volume:" + offlineTransferVO.getVolume() + " from:" + offlineTransferVO.getFrom() + " to:" + offlineTransferVO.getTo() + " source:" + user.getLoginSource());
                    if (offlineTransferVO.getFrom().equals(offlineTransferVO.getTo())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.VOLUME_IN_OUT_ERROR, "转入转出类型错误，不能选同类"));
                    }

                    //确保coinId与coinSymbol一致
                    Coin coin = coinService.findById(offlineTransferVO.getCoinId());
                    if(Objects.isNull(coin)){
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.VOLUME_IN_OUT_ERROR, "参数异常"));
                    }
                    offlineCoinVolumeService.inOut(user.getId(), offlineTransferVO.getCoinId(), offlineTransferVO.getVolume(), coin.getName(), offlineTransferVO.getFrom(), offlineTransferVO.getTo(), null);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("转入成功"));
                });
    }

    /**
     * 获取c2c资产余额
     *
     * @return
     */

    @GetMapping("/offline/volume/{coinId}")
    public Mono<GlobalMessageResponseVo> offlineVolume(@PathVariable("coinId") String coinId) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class)
                .flatMap(user -> Mono
                        .just(GlobalMessageResponseVo.newSuccessInstance(offlineCoinVolumeService.findByUserIdAndCoinId(user.getId(), coinId)
                        )));
    }

    /**
     * c2c币种转入转出记录
     *
     * @param offlineTransferListVO
     * @return
     */
    @PostMapping("/offline/coin/transfer/list")
    public Mono<GlobalMessageResponseVo> offlineTransferList(OfflineTransferListVO offlineTransferListVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String userId = e.getId();
                    offlineTransferListVO.setUserId(userId);
                    return GlobalMessageResponseVo.newSuccessInstance(offlineTransferLogService.findPage(offlineTransferListVO));

                });
    }

}

