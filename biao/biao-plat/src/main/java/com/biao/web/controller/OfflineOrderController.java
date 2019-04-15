package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.constant.SercurityConstant;
import com.biao.entity.OfflineOrder;
import com.biao.entity.PlatUser;
import com.biao.entity.UserBank;
import com.biao.enums.TradeEnum;
import com.biao.enums.UserCardStatusEnum;
import com.biao.enums.UserTagEnum;
import com.biao.limiter.RateLimiterHandler;
import com.biao.limiter.RedisRateLimiter;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.ResponsePage;
import com.biao.service.*;
import com.biao.vo.AdvertVO;
import com.biao.vo.OfflineAdvertVO;
import com.biao.vo.OfflineConfirmVO;
import com.biao.vo.OfflineOrderVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * c2c发布广告 最低500
 *
 *  ""azi
 */
@RestController
@RequestMapping("/biao")
public class OfflineOrderController {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OfflineOrderController.class);

    @Autowired
    private OfflineOrderService offlineOrderService;

    @Autowired
    private UserBankService userBankService;

    @Autowired
    private OfflineCancelLogService offlineCancelLogService;

    @Autowired
    private PlatUserService platUserService;

    @Autowired
    private OfflineCoinService offlineCoinService;

    @Autowired
    private RedisRateLimiter redisRateLimiter;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final long CACHE_TIME = 1; //缓存时间

    private static final String ORDER_QUERY_KEY = "order:query:%s:%s:%s:%s";// 广告查询缓存KEY

    /**
     * 需要实名 绑定银行卡
     *
     * @return
     */
    @GetMapping("/offline/topublish")
    public Mono<GlobalMessageResponseVo> topublish() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    final boolean lockTrade = user.isLockTrade();
                    if (lockTrade) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户被锁定不能交易"));
                    }
                    if (!UserCardStatusEnum.authRealName(user.getCardStatus(),user.getCountryCode())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请进行身份认证!"));
                    }
                    if (StringUtils.isBlank(user.getMobile())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_MOBILE_ERROR, "请绑定手机号!"));
                    }
                    String userId = user.getId();//"1";

                    final PlatUser platUser = platUserService.findById(userId);
                    if (Objects.nonNull(platUser)) {
                        final String c2cPublish = platUser.getC2cPublish();
                        if (org.apache.commons.lang.StringUtils.isNotBlank(c2cPublish)
                                && Integer.valueOf(c2cPublish) == 1) {
                            LOGGER.error("该用户已经被限制进行topublish 的交易 用户id为:{}", userId);
                            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "已经被限制进行topublish的交易 "));
                        }
                    }

                    //判定是否绑定银行卡
                    Optional<UserBank> userbank = userBankService.findByUserId(userId);
                    if (!userbank.isPresent()) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_BANK_ERROR, "请先绑定银行卡"));
                    }
                    if (StringUtils.isNotEmpty(user.getTag()) && (user.getTag().contains(UserTagEnum.FINANCE.getCode()) || user.getTag().contains(UserTagEnum.OTC_AGENT.getCode()))) {
                        LOGGER.info("用户id为:{},tag{}", userId, user.getTag());
                    } else {
                        long count = offlineCancelLogService.findCountByUserIdAndTypeAndDate(userId, "0", LocalDate.now());
                        if (count >= 3) {
                            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.PUBLISH_ADVERT_ERROR, "当天已撤销广告次数超3次，禁止发广告"));
                        }
                    }

                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("ok"));
                });
    }


    /**
     * 最小买入量 500 不需要实名
     * 卖出的广告最小成交量前端传入 买入的广告最小成交量看币种设置min_volume
     *
     * @param offlineAdvertVO
     * @param
     * @return buy 要判断银行卡是否绑定
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "币种数量不能为空"),
            @ValidateFiled(index = 0, filedName = "price", notNull = true, errMsg = "价格不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "币种符号不能为空"),
            @ValidateFiled(index = 0, filedName = "exType", isEnums = true, enums = "0,1", notNull = true, errMsg = "交易类型不正确")
    })
    @PostMapping("/offline/publish")
    public Mono<GlobalMessageResponseVo> publishLimiter(OfflineAdvertVO offlineAdvertVO) {
        if (offlineAdvertVO.getVolume().compareTo(BigDecimal.ZERO) == -1) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("广告数量非法"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    return redisRateLimiter.isAllowed(user.getId(), 1.0, 1.0, new RateLimiterHandler() {
                        @Override
                        public GlobalMessageResponseVo handler(Object[] param) {
                            {
                                String userId = user.getId();
                                String cardNo = "";
                                String bankName = "";
                                String bankBranchName = "";
                                final PlatUser platUser = platUserService.findById(userId);
                                if (Objects.nonNull(platUser)) {
                                    final String c2cPublish = platUser.getC2cPublish();
                                    if (org.apache.commons.lang.StringUtils.isNotBlank(c2cPublish)
                                            && Integer.valueOf(c2cPublish) == 1) {
                                        LOGGER.error("该用户已经被限制进行c2cPublish 的交易 用户id为:{}", userId);
                                        return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "已经被限制进行c2cPublish 的交易 ");
                                    }
                                }

                                if (offlineAdvertVO.getExType().equals(TradeEnum.SELL.ordinal()) && !UserCardStatusEnum.authRealName(user.getCardStatus(),user.getCountryCode())) {
                                    return GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请进行身份认证!");
                                }
                                if (offlineAdvertVO.getExType().equals(TradeEnum.SELL.ordinal())) {
                                    final boolean lockTrade = user.isLockTrade();
                                    if (lockTrade) {
                                        return GlobalMessageResponseVo.newErrorInstance("用户被锁定不能交易");
                                    }
                                    Optional<UserBank> userbank = userBankService.findByUserId(userId);
                                    if (!userbank.isPresent()) {
                                        return GlobalMessageResponseVo.newInstance(Constants.NO_BANK_ERROR, "请先绑定银行卡");
                                    } else {
                                        cardNo = userbank.get().getCardNo();
                                        bankName = userbank.get().getBankName();
                                        bankBranchName = userbank.get().getBranchBankName();
                                    }
                                    if (Objects.isNull(user.getMobile())) {
                                        return GlobalMessageResponseVo.newInstance(Constants.NO_MOBILE_ERROR, "请先绑定手机号");
                                    }
                                    //卖出需要进行交易验证
                                    String authKey = SercurityConstant.AUTH_USERNAME_REDIS_NAMESAPCE + user.getUsername();
                                    String authStirng = valOpsStr.get(authKey);
                                    if (authStirng == null || (!SercurityConstant.AuthType.PASS_AUTH.getCode().equals(authStirng))) {
                                        return GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_GOOGLE_CODE, "请验证交易方式");
                                    }

                                } else if (offlineAdvertVO.getExType().equals(TradeEnum.BUY.ordinal())) {
                                    if (Objects.isNull(user.getMobile())) {
                                        return GlobalMessageResponseVo.newInstance(Constants.NO_MOBILE_ERROR, "请先绑定手机号");
                                    }
                                }
                                final GlobalMessageResponseVo globalMessageResponseVo = offlineCoinService.checkVolumeAndPrice(offlineAdvertVO.getCoinId(),
                                        offlineAdvertVO.getPrice(), offlineAdvertVO.getVolume(), true);
                                if (globalMessageResponseVo.getCode().longValue() != Constants.SUCCESS_CODE) {
                                    return globalMessageResponseVo;
                                }
                                OfflineOrder offlineOrder = new OfflineOrder();
                                BeanUtils.copyProperties(offlineAdvertVO, offlineOrder);
                                offlineOrder.setUserId(userId);
                                offlineOrder.setRealName(user.getRealName());
                                offlineOrder.setMobile(user.getMobile());
                                offlineOrder.setWechatNo(user.getWechatNo());
                                offlineOrder.setWechatQrcodeId(user.getWechatQrcodeId());
                                offlineOrder.setAlipayNo(user.getAlipayNo());
                                offlineOrder.setAlipayQrcodeId(user.getAlipayQrcodeId());
                                offlineOrder.setCreateDate(LocalDateTime.now());
                                offlineOrder.setUpdateDate(LocalDateTime.now());
                                offlineOrder.setMinExVolume(offlineOrder.getMinExVolume() == null ? BigDecimal.ZERO : offlineOrder.getMinExVolume());
                                offlineOrder.setCardNo(cardNo);
                                offlineOrder.setBankBranchName(bankBranchName);
                                offlineOrder.setBankName(bankName);
                                offlineOrderService.save(offlineOrder, user.getTag());
                                return GlobalMessageResponseVo.newSuccessInstance("发布广告成功");
                            }
                        }
                    }, new Object[]{user, offlineAdvertVO});
                });
    }


    /**
     * 获取广告列表 某个币种 按照价格升序
     *
     * @param
     * @return
     */
    @PostMapping("/offline/gadvert/list")
    public Mono<GlobalMessageResponseVo> advertList(AdvertVO advertVO) {
        advertVO.setShowCount(10); // 默认10条每页，避免缓存由于每页数目不同导致数据过多
        String cacheKey = String.format(ORDER_QUERY_KEY, advertVO.getCoinId(), advertVO.getExType(), advertVO.getCurrentPage(), advertVO.getShowCount());
        ResponsePage<OfflineOrder> page = (ResponsePage<OfflineOrder>) redisTemplate.opsForValue().get(cacheKey);
        if (Objects.isNull(page)) {
            page = offlineOrderService.findPage(advertVO);
            redisTemplate.opsForValue().set(cacheKey, page, CACHE_TIME, TimeUnit.SECONDS);
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(page));
    }

    /**
     * 我发布的广告列表
     *
     * @return
     */
    @PostMapping("/offline/myAdvert/list")
    public Mono<GlobalMessageResponseVo> myAdvertList(OfflineOrderVO offlineOrderVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    offlineOrderVO.setUserId(userId);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineOrderService.findMyAdvertList(offlineOrderVO)));
                });
    }

    /**
     * 广告撤销
     *
     * @return
     */
    @PostMapping("/offline/gadvert/cancel")
    public Mono<GlobalMessageResponseVo> myAdverCancel(OfflineConfirmVO offlineConfirmVO) {
        //查询当天广告撤销次数  如果>=3则广告不能被撤销

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    if (StringUtils.isNotEmpty(user.getTag()) && (user.getTag().contains(UserTagEnum.FINANCE.getCode()) || user.getTag().contains(UserTagEnum.OTC_AGENT.getCode()))) {

                    } else {
                        long count = offlineCancelLogService.findCountByUserIdAndTypeAndDate(userId, "0", LocalDate.now());
                        if (count >= 3) {
                            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.CANCEL_ADVERT_ERROR, "当天已撤销广告次数超3次,禁止撤回广告"));
                        }

                    }
                    offlineConfirmVO.setUserId(userId);
                    offlineOrderService.advertCancel(offlineConfirmVO);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("取消成功"));
                });
    }

    @GetMapping("/offline/advert/cancel/ll")
    public Mono<GlobalMessageResponseVo> advertCancel() {

        offlineOrderService.findByStatusAndExType();
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("ok"));
    }
}
