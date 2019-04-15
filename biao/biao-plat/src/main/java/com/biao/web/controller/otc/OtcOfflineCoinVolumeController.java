package com.biao.web.controller.otc;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.OfflineCoinVolume;
import com.biao.entity.PlatUser;
import com.biao.entity.UserCoinVolume;
import com.biao.entity.otc.OtcOfflineCoin;
import com.biao.enums.AppSourceTypeEnum;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.CoinService;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.PlatUserService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.service.otc.OtcAccountSecretService;
import com.biao.service.otc.OtcOfflineCoinService;
import com.biao.utils.ServerWebExchangeUtils;
import com.biao.vo.CoinVolumeVO;
import com.biao.vo.OfflineTransferVO;
import com.biao.vo.OfflineVolumeVO;
import com.biao.vo.otc.OtcCoinVolumeVO;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;

/**
 * OTC账户查询
 *
 *  ""ong
 */
@RestController
@RequestMapping("/biao")
public class OtcOfflineCoinVolumeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtcOfflineCoinVolumeController.class);

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    @Autowired
    private OtcOfflineCoinService otcOfflineCoinService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeExService;

    @Autowired
    private OtcAccountSecretService otcAccountSecretService;

    @Autowired
    private PlatUserService platUserService;

    /**
     * 查询OTC账户余额
     *
     * @return
     */
    @GetMapping("/otc/volume/mylist")
    public Mono<GlobalMessageResponseVo> findOtcMyAll() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    //查找otc所有币种
                    List<OtcOfflineCoin> list = otcOfflineCoinService.findAll();
                    List<OfflineVolumeVO> listVo = new ArrayList<>(5);
                    list.forEach(offlineCoin -> {
                        OfflineVolumeVO offlineVolumeVO = new OfflineVolumeVO();
                        BeanUtils.copyProperties(offlineCoin, offlineVolumeVO);
                        listVo.add(offlineVolumeVO);
                    });
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
                    LOGGER.info("查询OTC账户资产结束listVo。。。。", listVo);
                    return GlobalMessageResponseVo
                            .newSuccessInstance(listVo);
                });
    }

    /**
     * 查询OTC账户余额, 免登陆
     *
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "mailOrMobileOrId", notNull = true, errMsg = "邮箱或手机号或用户ID不能为空"),
            @ValidateFiled(index = 0, filedName = "publishSource", notNull = true, errMsg = "来源不能为空"),
            @ValidateFiled(index = 0, filedName = "key", notNull = true, errMsg = "秘钥不能为空"),
    })
    @PostMapping("/otc/volume/user")
    public Mono<GlobalMessageResponseVo> findOtcAllByParam(OtcCoinVolumeVO otcCoinVolumeVO, ServerWebExchange exchange) {

        // 校验秘钥和来源
        Map<String, String> param = new HashMap<>();
        param.put("mailOrMobileOrId", otcCoinVolumeVO.getMailOrMobileOrId());
        param.put("publishSource", otcCoinVolumeVO.getPublishSource());
        String remoteAddr = ServerWebExchangeUtils.getRemoteAddr(exchange);
        otcAccountSecretService.checkAccountSecret(remoteAddr, otcCoinVolumeVO.getKey(), otcCoinVolumeVO.getPublishSource(), param);

        // 查询用户
        PlatUser platUser = platUserService.findByLoginName(otcCoinVolumeVO.getMailOrMobileOrId());
        if (Objects.isNull(platUser)) {
            platUser = platUserService.findById(otcCoinVolumeVO.getMailOrMobileOrId());
        }
        if (Objects.isNull(platUser)){
           return Mono.just(GlobalMessageResponseVo.newInstance(Constants.PARAM_ERROR, "手机号或邮箱或ID输入有误!"));
        }
        // 查询资产
        List<OtcOfflineCoin> list = otcOfflineCoinService.findAll();
        List<OfflineVolumeVO> listVo = new ArrayList<>(5);
        list.forEach(offlineCoin -> {
            OfflineVolumeVO offlineVolumeVO = new OfflineVolumeVO();
            BeanUtils.copyProperties(offlineCoin, offlineVolumeVO);
            listVo.add(offlineVolumeVO);
        });
        List<OfflineCoinVolume> listVolume = offlineCoinVolumeService.findAll(platUser.getId());
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
        LOGGER.info("查询OTC账户资产结束listVo。。。。", listVo);
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(listVo));

    }

    /**
     * 用户资产列表, 由于原来的接口会出现token过期返回结果为空情况，导致OTC无法判断
     *
     * @return
     */
    @GetMapping("/otc/coin/volume/list")
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
    @PostMapping("/otc/volume/inOut")
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
                    Coin coin = coinService.findById(offlineTransferVO.getCoinId());
                    if (Objects.isNull(coin) || !coin.getName().equals(offlineTransferVO.getSymbol())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.GLOBAL_ERROR_CODE,"币种ID错误或与币种符号不匹配", null));
                    }
                    offlineCoinVolumeService.inOut(user.getId(), offlineTransferVO.getCoinId(), offlineTransferVO.getVolume(),
                            offlineTransferVO.getSymbol(), offlineTransferVO.getFrom(), offlineTransferVO.getTo(), AppSourceTypeEnum.OTC.getCode());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("转入成功"));
                });
    }
}
