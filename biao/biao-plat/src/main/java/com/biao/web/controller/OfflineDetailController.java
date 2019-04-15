package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.entity.OfflineOrderDetail;
import com.biao.entity.UserBank;
import com.biao.enums.OfflineOrderDetailStatusEnum;
import com.biao.enums.UserCardStatusEnum;
import com.biao.enums.UserTagEnum;
import com.biao.exception.PlatException;
import com.biao.message.MessageManager;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.reactive.data.mongo.service.MessageService;
import com.biao.service.*;
import com.biao.service.push.OfflineOrderPushService;
import com.biao.util.DateTimeUtils;
import com.biao.vo.OfflineConfirmVO;
import com.biao.vo.OfflineTradeVO;
import com.biao.vo.OrderDetailVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * c2c买卖
 *
 *  ""azi
 */
@RestController
@RequestMapping("/biao")
@SuppressWarnings("all")
public class OfflineDetailController {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OfflineDetailController.class);

    @Autowired
    private OfflineOrderDetailService offlineOrderDetailService;
    @Autowired
    private UserBankService userBankService;


    @Autowired
    private OfflineCoinService offlineCoinService;

    @Value("${remainingTime:30}")
    private Integer remainingTime;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PlatUserService platUserService;

    @Autowired
    private ImOrderService imOrderService;

    @Autowired
    private OfflineOrderPushService offlineOrderPushService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageManager messageManager;

    /**
     * 用户只要下单  就立马将对应的金额 从 广告锁定 -》交易锁定  买家就只能撤销 广告锁定部分
     *
     * @param offlineTradeVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "币种符号不能为空"),
            @ValidateFiled(index = 0, filedName = "orderId", notNull = true, errMsg = "广告id不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "币种数量不能为空"),
    })
    @RequestMapping("/offline/buy")
    public Mono<GlobalMessageResponseVo> buyIn(OfflineTradeVO offlineTradeVO) {

        if (offlineTradeVO.getVolume().compareTo(BigDecimal.ZERO) == -1) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("买入数量非法"));
        }

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    String tag = user.getTag();
                    //判断用户是否实名
                    if (!UserCardStatusEnum.authRealName(user.getCardStatus(),user.getCountryCode())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请进行身份认证!"));
                    }

                    String cancelKey = userId+ DateTimeUtils.format("yyyy-MM-dd");

                    if (null != tag && (tag.contains(UserTagEnum.FINANCE.getCode()) || tag.contains(UserTagEnum.OTC_AGENT.getCode()))) {
                        LOGGER.info("财务购买{}",userId);
                    }else{
                        String redisCancelCount = valOpsStr.get(cancelKey);
                        if(StringUtils.isEmpty(redisCancelCount)){

                        }else{
                            Long cancelCount = Long.parseLong(redisCancelCount);
                            if(cancelCount>=2L){
                                throw new PlatException(Constants.OFFLINE_ORDER_CANCEL_MORE_ERROR, "当天撤销订单次数太多");
                            }
                        }
                    }
                    //买入不需要   判定是否绑定银行卡
                    /*Optional<UserBank> userbank = userBankService.findByUserId(userId);
                    if (!userbank.isPresent()) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_BANK_ERROR, "请先绑定银行卡"));
                    }*/
                    if (Objects.isNull(user.getMobile())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_MOBILE_ERROR, "请先绑定手机号"));
                    }

                    final GlobalMessageResponseVo globalMessageResponseVo =
                            offlineCoinService.checkVolumeAndPrice(offlineTradeVO.getCoinId(),
                                    null, offlineTradeVO.getVolume(), false);
                    if (globalMessageResponseVo.getCode().longValue() != Constants.SUCCESS_CODE) {
                        return Mono.just(globalMessageResponseVo);
                    }

                    OfflineOrderDetail offlineOrderDetail = new OfflineOrderDetail();
                    offlineOrderDetail.setUserId(userId);
                    offlineOrderDetail.setUserName(user.getRealName());
                    offlineOrderDetail.setUserMobile(user.getMobile());
                    offlineOrderDetail.setOrderId(offlineTradeVO.getOrderId());
                    offlineOrderDetail.setVolume(offlineTradeVO.getVolume());
                    String orderId = offlineOrderDetailService.buy(offlineOrderDetail);
                    Map<String, Object> returnData = new HashMap<>();
                    returnData.put("orderId", orderId);
                    syncPushData(userId, offlineOrderDetail.getSubOrderId(), Integer.parseInt(OfflineOrderDetailStatusEnum.NOMAL.getCode()));
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(returnData));
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
    @PostMapping("/offline/buy/confirm")
    public Mono<GlobalMessageResponseVo> buyConfirm(OfflineConfirmVO offlineConfirmVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    //可以根据userid suborderid 来确认是否该用户的订单
                    Integer status = Integer.parseInt(OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode());
                    offlineConfirmVO.setStatus(status);
                    //判断订单状态  只能是待支付情况 才能支付 其他场景不能支付
                    offlineOrderDetailService.updateStatusBySubOrderId(offlineConfirmVO);
                    syncPushData(user.getId(), offlineConfirmVO.getSubOrderId(), status);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(status));
                });
    }


    private void syncPushData(String userId, String subOrderId, Integer status) {
        offlineOrderPushService.pushData(userId, subOrderId, status);
        messageManager.saveAndPushMessage(userId, subOrderId, status);
    }


    /**
     * 将币卖出 有人挂买单 冻结卖家资产 从volume-》 lockVolume
     *
     * @param offlineTradeVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "orderId", notNull = true, errMsg = "广告id不能为空"),
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "币种符号不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "币种数量不能为空")
    })
    @PostMapping("/offline/sell")
    public Mono<GlobalMessageResponseVo> sell(OfflineTradeVO offlineTradeVO) {
        if (offlineTradeVO.getVolume().compareTo(BigDecimal.ZERO) == -1) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("卖出数量非法"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    final boolean lockTrade = user.isLockTrade();
                    if (lockTrade) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户被锁定不能交易"));
                    }
                    //判断用户是否实名
                    if (!UserCardStatusEnum.authRealName(user.getCardStatus(), user.getCountryCode())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请进行身份认证!"));
                    }
                    //判定是否绑定银行卡
                    Optional<UserBank> userbank = userBankService.findByUserId(userId);
                    if (!userbank.isPresent()) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_BANK_ERROR, "请先绑定银行卡"));
                    }
                    if (Objects.isNull(user.getMobile())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_MOBILE_ERROR, "请先绑定手机号"));
                    }

                    //卖出需要进行交易验证  去掉了

                    final GlobalMessageResponseVo globalMessageResponseVo =
                            offlineCoinService.checkVolumeAndPrice(offlineTradeVO.getCoinId(),
                                    null, offlineTradeVO.getVolume(), true);
                    if (globalMessageResponseVo.getCode().longValue() != Constants.SUCCESS_CODE) {
                        return Mono.just(globalMessageResponseVo);
                    }
                    //判断用户是否绑定银行卡
                    OfflineOrderDetail offlineOrderDetail = new OfflineOrderDetail();
                    offlineOrderDetail.setOrderId(offlineTradeVO.getOrderId());
                    offlineOrderDetail.setVolume(offlineTradeVO.getVolume());
                    offlineOrderDetail.setUserName(user.getRealName());
                    String orderId = offlineOrderDetailService.sell(offlineOrderDetail, userId, user.getMobile(), user.getTag(), user.getAlipayNo(), user.getAlipayQrcodeId(), user.getWechatNo(), user.getWechatQrcodeId(), userbank.get());
                    syncPushData(userId, offlineOrderDetail.getSubOrderId(), Integer.parseInt(OfflineOrderDetailStatusEnum.NOMAL.getCode()));
                    Map<String, Object> returnData = new HashMap<>();
                    returnData.put("orderId", orderId);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(returnData));
                });
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
            @ValidateFiled(index = 0, filedName = "orderId", notNull = true, errMsg = "广告id不能为空"),
            @ValidateFiled(index = 0, filedName = "subOrderId", notNull = true, errMsg = "订单号不能为空"),
    })
    @RequestMapping("/offline/sell/confirm")
    public Mono<GlobalMessageResponseVo> sellConfirm(OfflineConfirmVO offlineConfirmVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    offlineConfirmVO.setUserId(userId);
                    offlineOrderDetailService.updateStatusBySubOrderIdAndUnLock(offlineConfirmVO, user.getTag());

                    Integer status = Integer.parseInt(OfflineOrderDetailStatusEnum.CONFIRM_IN.getCode());
                    syncPushData(user.getId(), offlineConfirmVO.getSubOrderId(), status);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(status));
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
    @RequestMapping("/offline/detail/cancel")
    public Mono<GlobalMessageResponseVo> detailCancel(OfflineConfirmVO offlineConfirmVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    offlineConfirmVO.setUserId(userId);
                    offlineOrderDetailService.detailCancel(offlineConfirmVO,user.getTag());
                    Integer status = Integer.parseInt(OfflineOrderDetailStatusEnum.CANCEL.getCode());
                    /*   syncPushData(user.getId(), offlineConfirmVO.getSubOrderId(), status);*/
                    messageManager.saveAndPushMessage(userId, offlineConfirmVO.getSubOrderId(), status);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(status));
                });
    }

    /**
     * 我的订单列表
     *
     * @param
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "status", isEnums = true, enums = "0,2,9,4", notNull = true, errMsg = "接口错误"),
    })
    @PostMapping("/offline/myOrderDetail/list")
    public Mono<GlobalMessageResponseVo> myOrderDetail(OrderDetailVO orderDetailVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    //List<OfflineOrderDetail> list = offlineOrderDetailService.myOrderDetail(userId);
                    orderDetailVO.setUserId(userId);
                    if (StringUtils.isNotBlank(orderDetailVO.getStatus()) && orderDetailVO.getStatus().equals("0")) {
                        orderDetailVO.setStatus("0,1");
                    }
                    if (StringUtils.isNotBlank(orderDetailVO.getStatus()) && orderDetailVO.getStatus().equals("4")) {
                        orderDetailVO.setStatus("0,1,2,9");
                    }
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineOrderDetailService.myOrderDetail(orderDetailVO)));
                });
    }


    /**
     * 确定在何种状态下才可以进行申诉 1：取消状态下不应该执行申诉
     *
     * @param offlineConfirmVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "subOrderId", notNull = true, errMsg = "订单号不能为空"),
    })
    @RequestMapping("/offline/detail/shensu")
    public Mono<GlobalMessageResponseVo> detailShensu(OfflineConfirmVO offlineConfirmVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    offlineConfirmVO.setUserId(userId);
                    offlineOrderDetailService.detailShensu(offlineConfirmVO);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("ok"));
                });
    }


    @GetMapping("/offline/detail/{orderId}")
    public Mono<GlobalMessageResponseVo> findByOfflineOrder(@PathVariable("orderId") String orderId) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineOrderDetailService.findByOrderId(userId, orderId)));
                });
    }

    @GetMapping("/offline/orderDetail/sub/{orderId}")
    public Mono<GlobalMessageResponseVo> findOfflineOrderBySubId(@PathVariable("orderId") String orderId) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineOrderDetailService.findByUserIdAndOrderId(userId, orderId)));
                });
    }
    
    @GetMapping("/offline/orderDetail/{id}")
    public Mono<GlobalMessageResponseVo> findOfflineOrderDetailById(@PathVariable("id") String id) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    OfflineOrderDetail offlineOrderDetail = offlineOrderDetailService.findById(id);
                    if (!offlineOrderDetail.getUserId().equalsIgnoreCase(userId)) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("查询非法"));
                    }
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineOrderDetailService.findById(id)));
                });
    }

}















