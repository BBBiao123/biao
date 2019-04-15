package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.Coin;
import com.biao.entity.SuperBookConf;
import com.biao.entity.UserCoinVolume;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.ResponsePage;
import com.biao.query.UserTradeQuery;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.service.CoinService;
import com.biao.service.Mk2MiningService;
import com.biao.service.OrderService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.vo.CoinVolumeVO;
import com.biao.vo.UserTradeVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户资产相关操作
 */
@RestController
@RequestMapping("/biao")
public class UserCoinVolumeController {
    @Autowired
    private UserCoinVolumeExService userCoinVolumeExService;
    @Autowired
    private CoinService coinService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private Mk2MiningService mk2MiningService;

    /**
     * 用户资产列表
     *
     * @return
     */
    @GetMapping("/coin/volume/list")
    public Mono<GlobalMessageResponseVo> findAll() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    //查找coin所有币种
                    List<Coin> list = coinService.findAll();
                    List<CoinVolumeVO> listVo = new ArrayList<>(5);
                    list.forEach(coin -> {
                        CoinVolumeVO coinVolumeVO = new CoinVolumeVO();
                        BeanUtils.copyProperties(coin, coinVolumeVO);
                        listVo.add(coinVolumeVO);

                        // UES是否显示超级账本地址
                        if (coin.getName().equals("UES")) {
                            SuperBookConf bookConf = mk2MiningService.findSuperBookBySymbol("UES");
                            if (Objects.nonNull(bookConf)) {
                                coinVolumeVO.setShowSuperBook(bookConf.getStatus());
                            }
                        }
                    });
                    List<UserCoinVolume> listVolume = userCoinVolumeExService.findAll(e.getId());
                    if (CollectionUtils.isNotEmpty(listVolume)) {
                        listVo.forEach(coinVolumeVO2 -> {
                            listVolume.forEach(userCoinVolume -> {
                                if (userCoinVolume != null) {
                                    if (userCoinVolume.getCoinSymbol().equals(coinVolumeVO2.getName())) {
                                        String id = coinVolumeVO2.getId();
                                        BeanUtils.copyProperties(userCoinVolume, coinVolumeVO2);
                                        coinVolumeVO2.setId(id);
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
     * 获取bb对应的币种资产
     *
     * @return
     */

    @GetMapping("/coin/volume/{coinId}")
    public Mono<GlobalMessageResponseVo> coinVolume(@PathVariable("coinId") String coinId) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class)
                .flatMap(user -> Mono
                        .just(GlobalMessageResponseVo.newSuccessInstance(userCoinVolumeExService.findByUserIdAndCoinId(user.getId(), coinId)
                        )));
    }

    /**
     * 获取bb对应的币种资产
     *
     * @return
     */

    @GetMapping("/coin/volume/symbol/{coinSymbol}")
    public Mono<GlobalMessageResponseVo> coinSymbol(@PathVariable("coinSymbol") String coinSymbol) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class)
                .flatMap(user -> Mono
                        .just(GlobalMessageResponseVo.newSuccessInstance(userCoinVolumeExService.findByUserIdAndCoinSymbol(user.getId(), coinSymbol)
                        )));
    }


    /**
     * 获取用户当前挂单
     *
     * @param userTradeQuery 查询条件
     * @return Mono<GlobalMessageResponseVo>
     */
    @PostMapping("/coin/userTradeOrder")
    public Mono<GlobalMessageResponseVo> userTradeOrder(UserTradeQuery userTradeQuery) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class)
                .flatMap(user -> {
                            userTradeQuery.setUserId(user.getId());
                            ResponsePage<UserTradeVO> voResponsePage = orderService.findByPage(userTradeQuery);
                            return Mono.just(GlobalMessageResponseVo.newSuccessInstance(voResponsePage));

                        }
                );
    }

    /**
     * 根据订单号获取交易详情
     *
     * @param orderNo 订单号
     * @return Mono<GlobalMessageResponseVo>
     */
    @GetMapping("/coin/tradeOrderDetail/{orderNo}")
    public Mono<GlobalMessageResponseVo> tradeOrderDetail(@PathVariable("orderNo") String orderNo) {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(tradeDetailService.findByOrderNo(orderNo)));
    }


    /**
     * 获取用户成交明细
     *
     * @param userTradeQuery 查询条件
     * @return Mono<GlobalMessageResponseVo>
     */
    @PostMapping("/coin/userTradeOrderDetail")
    public Mono<GlobalMessageResponseVo> userTradeOrderDetail(UserTradeQuery userTradeQuery) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class)
                .flatMap(user -> {
                            userTradeQuery.setUserId(user.getId());
                            ResponsePage<UserTradeVO> voResponsePage = tradeDetailService.findByPage(userTradeQuery);
                            return Mono.just(GlobalMessageResponseVo.newSuccessInstance(voResponsePage));

                        }
                );
    }


}
