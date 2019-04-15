package com.biao.web.controller.otc;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.constant.TradeConstant;
import com.biao.entity.otc.OtcOfflineOrderDetail;
import com.biao.entity.otc.OtcUserBank;
import com.biao.enums.OfflineOrderDetailStatusEnum;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.PlatUserService;
import com.biao.service.otc.OtcOfflineCoinService;
import com.biao.service.otc.OtcOfflineOrderDetailService;
import com.biao.service.otc.OtcUserBankService;
import com.biao.vo.otc.OtcOfflineOrderDetailVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 * OTC订单操作
 */
@RestController
@RequestMapping("/biao")
public class OtcOfflineOrderDetailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtcOfflineOrderDetailController.class);

    @Autowired
    private OtcOfflineOrderDetailService otcOfflineOrderDetailService;

    @Autowired
    private OtcUserBankService otcUserBankService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${remainingTime:30}")
    private Integer remainingTime;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PlatUserService platUserService;

    @Autowired
    private OtcOfflineCoinService offlineCoinService;

    /**
     * 根据订单ID查询广告支付方式
     * 注：放此处为保护广告人支付信息，只有对广告下订单对人才能查看
     *
     * @param subOrderId
     * @return
     */
//    @GetMapping("/otc/orderpay/{subOrderId}")
    public Mono<GlobalMessageResponseVo> findPayByOfflineSubOrder(@PathVariable("subOrderId") String subOrderId) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcOfflineOrderDetailService.findPayByOfflineSubOrder(userId, subOrderId)));
                });
    }

    /**
     * 根据订单ID查询订单详细信息
     *
     * @param subOrderId
     * @return
     */
//    @GetMapping("/otc/detail/{subOrderId}")
    public Mono<GlobalMessageResponseVo> findBySubOrderId(@PathVariable("subOrderId") String subOrderId) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcOfflineOrderDetailService.findByUserIdAndDetailId(userId, subOrderId, user.getLoginSource())));
                });
    }

    /**
     * 统计查询订单条数，未付款数，已付款数
     *
     * @return
     */
//    @GetMapping("/otc/detail/count")
    public Mono<GlobalMessageResponseVo> countOrderDetail() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcOfflineOrderDetailService.countOrderDetail(userId)));
                });
    }

    /**
     * 按状态查询我的订单
     *
     * @return
     */
//    @PostMapping("/otc/detail/findbystatus")
    public Mono<GlobalMessageResponseVo> findSubOrders(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    otcOfflineOrderDetailVO.setUserId(user.getId());
                    otcOfflineOrderDetailVO.setLoginSource(user.getLoginSource());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcOfflineOrderDetailService.findMySuborderByStatus(otcOfflineOrderDetailVO)));
                });
    }


    /**
     * 获取我的所有订单
     *
     * @return
     */
//    @PostMapping("/otc/detail/findall")
    public Mono<GlobalMessageResponseVo> findAllSubOrder(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    otcOfflineOrderDetailVO.setUserId(user.getId());
                    otcOfflineOrderDetailVO.setLoginSource(otcOfflineOrderDetailVO.getLoginSource());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcOfflineOrderDetailService.findAllSubOrder(otcOfflineOrderDetailVO)));
                });
    }

    /**
     * 用户只要下单  就立马将对应的金额 从 广告锁定 -》交易锁定  买家就只能撤销 广告锁定部分
     *
     * @param otcOfflineOrderDetailVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "币种符号不能为空"),
            @ValidateFiled(index = 0, filedName = "orderId", notNull = true, errMsg = "广告id不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "币种数量不能为空"),
    })
//    @RequestMapping("/otc/buy")
    public Mono<GlobalMessageResponseVo> buyIn(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    if (Objects.isNull(user.getMobile())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_MOBILE_ERROR, "请先绑定手机号"));
                    }

                    final GlobalMessageResponseVo globalMessageResponseVo =
                            offlineCoinService.checkVolumeAndPrice(otcOfflineOrderDetailVO.getCoinId(),
                                    null, otcOfflineOrderDetailVO.getVolume(), false);
                    if (globalMessageResponseVo.getCode().longValue() != Constants.SUCCESS_CODE) {
                        return Mono.just(globalMessageResponseVo);
                    }

                    OtcOfflineOrderDetail offlineOrderDetail = new OtcOfflineOrderDetail();
                    BeanUtils.copyProperties(otcOfflineOrderDetailVO, offlineOrderDetail);
                    offlineOrderDetail.setUserId(userId);
                    offlineOrderDetail.setUserMobile(user.getMobile());
                    offlineOrderDetail.setRealName(user.getRealName());
                    offlineOrderDetail.setPublishSource(user.getLoginSource());
                    otcOfflineOrderDetailService.buy(offlineOrderDetail);

//                    syncPushData(userId, offlineOrderDetail.getSubOrderId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineOrderDetail));
                });
    }

    /**
     * 买家确认打钱
     *
     * @param offlineConfirmVO 交易详情订单id
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "subOrderId", notNull = true, errMsg = "订单号不能为空"),
    })
//    @PostMapping("/otc/buy/confirm")
    public Mono<GlobalMessageResponseVo> buyConfirm(OtcOfflineOrderDetailVO offlineConfirmVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    offlineConfirmVO.setLoginSource(user.getLoginSource());
                    offlineConfirmVO.setUserId(user.getId());
                    otcOfflineOrderDetailService.confirmPayment(offlineConfirmVO);
//                    syncPushData(user.getId(), offlineConfirmVO.getSubOrderId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode()));
                });
    }

    /**
     * 将币卖出 有人挂买单 冻结卖家资产 从volume-》 lockVolume
     *
     * @param orderDetailVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "orderId", notNull = true, errMsg = "广告id不能为空"),
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "币种符号不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "币种数量不能为空"),
    })
//    @PostMapping("/otc/sell")
    public Mono<GlobalMessageResponseVo> sell(OtcOfflineOrderDetailVO orderDetailVO) {
        if (orderDetailVO.getVolume().compareTo(BigDecimal.ZERO) == -1) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("卖出数量非法"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    Mono<GlobalMessageResponseVo> checkResult = checkSell(orderDetailVO, user);
                    if (Objects.nonNull(checkResult)) {
                        return checkResult;
                    }
                    // OTC币种校验，最大最小，币种是否可以支持OTC交易
                    final GlobalMessageResponseVo globalMessageResponseVo =
                            offlineCoinService.checkVolumeAndPrice(orderDetailVO.getCoinId(),
                                    null, orderDetailVO.getVolume(), true);
                    if (globalMessageResponseVo.getCode().longValue() != Constants.SUCCESS_CODE) {
                        return Mono.just(globalMessageResponseVo);
                    }
                    // 下单
                    OtcOfflineOrderDetail offlineOrderDetail = new OtcOfflineOrderDetail();
                    BeanUtils.copyProperties(orderDetailVO, offlineOrderDetail);
                    offlineOrderDetail.setUserId(user.getId());
                    offlineOrderDetail.setUserMobile(user.getMobile());
                    offlineOrderDetail.setPublishSource(user.getLoginSource());
                    offlineOrderDetail.setRealName(user.getRealName());
                    otcOfflineOrderDetailService.sell(offlineOrderDetail, orderDetailVO.getDetailPay());
//                    syncPushData(userId, offlineOrderDetail.getSubOrderId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineOrderDetail));
                });
    }

    private Mono<GlobalMessageResponseVo> checkSell(OtcOfflineOrderDetailVO offlineTradeVO, final RedisSessionUser user) {
        String userId = user.getId();
        //判断用户是否实名
//        if (!user.getCardStatus().toString().equals(UserCardStatusEnum.PASS.getCode())) {
//            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请进行身份认证!"));
//        }
        //判定是否绑定银行卡
        List<OtcUserBank> userbanks = otcUserBankService.findByUserIdAndStatus(user.getId(), "1");
        if (CollectionUtils.isEmpty(userbanks)) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_BANK_ERROR, "请先绑定银行卡"));
        }
        if (Objects.isNull(user.getMobile())) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_MOBILE_ERROR, "请先绑定手机号"));
        }

        //卖出需要进行交易验证
        //1、判断交易验证码类型进行验证
//        String decodeExPassword = offlineTradeVO.getExPassword();
//        if (StringUtils.isBlank(decodeExPassword)) {
//            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "验证交易参数不正确"));
//        }
//        String validTimeKey = new StringBuilder("c2s:trade:sell:")
//                .append(DateUtil.formatDate()).append(":").append(userId).toString();
//        Long errorTime = valOpsStr.increment(validTimeKey, 1);
//        valOpsStr.getOperations().expire(validTimeKey, 3600 * 24, TimeUnit.SECONDS);
//        if (errorTime > 5) {
//            //一天登录大于5次不能登录了
//            LOGGER.error("userId:{},交易密码错误次数超过限制,当天限制交易", userId);
//            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_OVER_TIMES_ERROR, "交易密码错误次数超过限制,当天限制交易"));
//        }
//        String decryPassword = RsaUtils.decryptByPrivateKey(decodeExPassword, RsaUtils.DEFAULT_PRIVATE_KEY);
//        Integer exValidType = user.getExValidType();
//        if (exValidType == null) {
//            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "请先设置交易验证方式"));
//        } else if (exValidType == 1) {
//            //谷歌验证 进行谷歌验证
//            if (!StringHelp.regexMatcher("\\d+", decryPassword)) {
//                return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "请输入正确的谷歌验证码"));
//            }
//            //查询最新用户
//            PlatUser platUser = platUserService.findById(userId);
//            if (StringUtils.isBlank(platUser.getGoogleAuth())) {
//                //谷歌验证
//                return Mono.just(GlobalMessageResponseVo.newErrorInstance("请重设交易验证"));
//            }
//            GoogleAuthenticator ga = new GoogleAuthenticator();
//            ga.setWindowSize(5);
//            boolean result = ga.checkCode(platUser.getGoogleAuth(), Long.parseLong(decryPassword), System.currentTimeMillis());
//            if (!result) {
//                LOGGER.error("该用户id为:{},发布广告,谷歌验证失败", userId);
//                return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "谷歌验证失败"));
//            }
//        } else if (exValidType == 2) {
//            //交易密码验证 进行交易密码验证
//            boolean result = passwordEncoder.matches(decryPassword, user.getExPassword());
//            if (!result) {
//                LOGGER.error("该用户id为:{},发布广告,交易密码验证失败", userId);
//                return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "交易密码验证失败"));
//            }
//        } else {
//            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "请先设置交易验证方式"));
//        }
        //交易验证成功取消限制
//        valOpsStr.getOperations().delete(validTimeKey);
        return null;
    }

    /**
     * 挂卖单 者确认收到钱  这个一定要确定挂单身份才能执行 防止买家确认收款
     * 确认收钱 还要将该笔订单交易冻结金额
     * 确认收款 防止误操作 一定是 买家确认付款后的状态
     *
     * @param offlineConfirmVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "subOrderId", notNull = true, errMsg = "订单号不能为空"),
    })
//    @RequestMapping("/otc/sell/confirm")
    public Mono<GlobalMessageResponseVo> sellConfirm(OtcOfflineOrderDetailVO offlineConfirmVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    Mono<GlobalMessageResponseVo> checkResult = checkSell(offlineConfirmVO, user);
                    if (Objects.nonNull(checkResult)) {
                        return checkResult;
                    }
                    offlineConfirmVO.setUserId(user.getId());
                    offlineConfirmVO.setLoginSource(user.getLoginSource());
                    otcOfflineOrderDetailService.confirmReceipt(offlineConfirmVO);
//                    syncPushData(user.getId(), offlineConfirmVO.getSubOrderId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(OfflineOrderDetailStatusEnum.CONFIRM_IN.getCode()));
                });
    }

    /**
     * 撤销订单
     *
     * @param offlineConfirmVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "subOrderId", notNull = true, errMsg = "订单号不能为空"),
    })
//    @RequestMapping("/otc/detail/cancel")
    public Mono<GlobalMessageResponseVo> detailCancel(OtcOfflineOrderDetailVO offlineConfirmVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    offlineConfirmVO.setUserId(user.getId());
                    offlineConfirmVO.setLoginSource(user.getLoginSource());
                    otcOfflineOrderDetailService.cancelOrderDetail(offlineConfirmVO);
//                    syncPushData(user.getId(), offlineConfirmVO.getSubOrderId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(OfflineOrderDetailStatusEnum.CANCEL.getCode()));
                });
    }

    /**
     * 推送消息
     *
     * @param userId
     * @param subOrderId
     */
    private void syncPushData(String userId, String subOrderId, String loginSource) {

        OtcOfflineOrderDetail orderDetail = otcOfflineOrderDetailService.findByUserIdAndDetailId(userId, subOrderId, loginSource);
        //设置取消时间
        if (OfflineOrderDetailStatusEnum.NOMAL.getCode().equals(orderDetail.getStatus())) {
            final LocalDateTime endTime = orderDetail.getCreateDate().plusMinutes(remainingTime);
            final LocalDateTime now = LocalDateTime.now();
            if (now.isEqual(endTime) || now.isAfter(endTime)) {
                orderDetail.setRemainingTime(0);
            } else {
                final int until = (int) now.until(endTime, ChronoUnit.SECONDS);
                orderDetail.setRemainingTime(until);
            }
        }
        if (Objects.equals(orderDetail.getRemarks(), "buy")) {
            orderDetail.setRemarks("sell");
        } else {
            orderDetail.setRemarks("buy");
        }
        orderDetail.setCreateDate(null);
        orderDetail.setUpdateDate(null);
        kafkaTemplate.send(TradeConstant.C2C_USER_ORDER, SampleMessage.build(orderDetail));
        //platPushDataHandler.pushC2CData(askUserId, JsonUtils.toJson(orderDetail));
    }
}
