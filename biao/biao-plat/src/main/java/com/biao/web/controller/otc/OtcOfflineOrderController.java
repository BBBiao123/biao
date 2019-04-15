package com.biao.web.controller.otc;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.entity.PlatUser;
import com.biao.entity.otc.OtcOfflineOrder;
import com.biao.entity.otc.OtcUserBank;
import com.biao.enums.TradeEnum;
import com.biao.enums.UserCardStatusEnum;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.OfflineCancelLogService;
import com.biao.service.PlatUserService;
import com.biao.service.otc.OtcOfflineCoinService;
import com.biao.service.otc.OtcOfflineOrderService;
import com.biao.service.otc.OtcUserBankService;
import com.biao.vo.otc.OtcOfflineOrderVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * OTC广告操作
 */
@RestController
@RequestMapping("/biao")
public class OtcOfflineOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtcOfflineOrderController.class);

    @Autowired
    private OtcOfflineOrderService otcOfflineOrderService;

    @Autowired
    private PlatUserService platUserService;

    @Autowired
    private OtcUserBankService otcUserBankService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OfflineCancelLogService offlineCancelLogService;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    private OtcOfflineCoinService offlineCoinService;

    private static final String C2S_TREADE_KEY = "c2s:trade:sell:";

    /**
     * 我的广告列表
     *
     * @param
     * @return
     */
//    @GetMapping("/otc/myAdvert/list")
    public Mono<GlobalMessageResponseVo> myAdvertList(OtcOfflineOrderVO otcOfflineOrderVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    otcOfflineOrderVO.setUserId(user.getId());
                    otcOfflineOrderVO.setLoginSource(user.getLoginSource());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcOfflineOrderService.findMyAdvertList(otcOfflineOrderVO)));
                });
    }

    /**
     * 获取广告列表 某个币种 按照价格升序
     *
     * @param
     * @return
     */
//    @PostMapping("/otc/gadvert/list")
    public Mono<GlobalMessageResponseVo> advertList(OtcOfflineOrderVO otcOfflineOrderVO) {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcOfflineOrderService.findPage(otcOfflineOrderVO)));
    }

    /**
     * 需要实名 绑定银行卡
     *
     * @return
     */
//    @GetMapping("/otc/topublish")
    public Mono<GlobalMessageResponseVo> topublish() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    final PlatUser platUser = platUserService.findById(user.getId());
                    // 发广告前置校验
                    Mono<GlobalMessageResponseVo> checkResult = preCheck(user, platUser);
                    if (Objects.nonNull(checkResult)) {
                        return checkResult;
                    }
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("ok"));
                });
    }

    private Mono<GlobalMessageResponseVo> preCheck(RedisSessionUser user, PlatUser platUser) {
        if (!UserCardStatusEnum.authRealName(platUser.getCardStatus(),user.getCountryCode())) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请进行身份认证!"));
        }
        if (StringUtils.isBlank(user.getMobile())) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_MOBILE_ERROR, "请绑定手机号!"));
        }
        if (Objects.nonNull(platUser)) {
            if ("1".equals(platUser.getC2cPublish())) {
                LOGGER.error("该用户已经被限制进行topublish 的交易 用户id为:{}", user.getId());
                return Mono.just(GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "已经被限制进行topublish的交易 "));
            }
        } else {
            LOGGER.error("C2C广告用户数据有误id为:{}", user.getId());
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "C2C广告用户数据有误"));
        }
        //判定是否绑定银行卡
        OtcUserBank otcUserBank = new OtcUserBank();
        otcUserBank.setUserId(user.getId());
        List<OtcUserBank> userbanks = otcUserBankService.findByParam(otcUserBank);
        if (CollectionUtils.isEmpty(userbanks)) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.NO_BANK_ERROR, "请先绑定银行卡"));
        }

        return checkCancelCount(user);//
    }

    private Mono<GlobalMessageResponseVo> checkCancelCount(RedisSessionUser user) {
//        if (StringUtils.isNotEmpty(user.getTag()) && (user.getTag().contains(UserTagEnum.FINANCE.getCode()) || user.getTag().contains(UserTagEnum.OTC_AGENT.getCode()))) {
//            LOGGER.info("用户id为:{},tag{}", user.getId(), user.getTag());
//        } else {
//            long count = offlineCancelLogService.findCountByUserIdAndTypeAndDate(user.getId(), "0", LocalDate.now());
//            if (count >= 3) {
//                return Mono.just(GlobalMessageResponseVo.newInstance(Constants.PUBLISH_ADVERT_ERROR, "当天已撤销广告次数超3次，禁止发广告"));
//            }
//        }
        return null;
    }

    private Mono<GlobalMessageResponseVo> doCheck(RedisSessionUser user, PlatUser platUser, OtcOfflineOrderVO otcOfflineOrderVO) {

        if (!otcOfflineOrderVO.getExType().equals(TradeEnum.SELL.ordinal())) {
            return null;
        }

        //失效redis的key定义
//        String validTimeKey = new StringBuilder(C2S_TREADE_KEY).append(DateUtil.formatDate()).append(":").append(user.getId()).toString();
//        Long errorTime = valOpsStr.increment(validTimeKey, 1);
//        valOpsStr.getOperations().expire(validTimeKey, 3600 * 24, TimeUnit.SECONDS);
//        if (errorTime > 5) {
//            //一天登录大于5次不能登录了
//            LOGGER.error("userId:{},交易密码错误次数超过限制,当天限制交易", user.getId());
//            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_OVER_TIMES_ERROR, "交易密码错误次数超过限制,当天限制交易"));
//        }

        //卖出需要进行交易验证
        //1、判断交易验证码类型进行验证
//        String decodeExPassword = otcOfflineOrderVO.getExPassword();
//        if (StringUtils.isBlank(decodeExPassword)) {
//            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "验证交易参数不正确"));
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
//            if (StringUtils.isBlank(platUser.getGoogleAuth())) {
//                return Mono.just(GlobalMessageResponseVo.newErrorInstance("请重设交易验证"));//谷歌验证
//            }
//            GoogleAuthenticator ga = new GoogleAuthenticator();
//            ga.setWindowSize(5);
//            boolean result = ga.checkCode(platUser.getGoogleAuth(), Long.parseLong(decryPassword), System.currentTimeMillis());
//            if (!result) {
//                LOGGER.error("该用户id为:{},发布广告,谷歌验证失败", user.getId());
//                return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "谷歌验证失败"));
//            }
//        } else if (exValidType == 2) {
//            //交易密码验证 进行交易密码验证
//            boolean result = passwordEncoder.matches(decryPassword, user.getExPassword());
//            if (!result) {
//                LOGGER.error("该用户id为:{},发布广告,交易密码验证失败", user.getId());
//                return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "交易密码验证失败"));
//            }
//        } else {
//            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "请先设置交易验证方式"));
//        }
        //交易验证成功取消限制
//        valOpsStr.getOperations().delete(validTimeKey);

        // 币种和币价校验
        final GlobalMessageResponseVo globalMessageResponseVo = offlineCoinService.checkVolumeAndPrice(otcOfflineOrderVO.getCoinId(),
                otcOfflineOrderVO.getPrice(), otcOfflineOrderVO.getVolume(), true);
        if (globalMessageResponseVo.getCode().longValue() != Constants.SUCCESS_CODE) {
            return Mono.just(globalMessageResponseVo);
        }

        return null;
    }

    /**
     * 最小买入量 500 不需要实名
     *
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
//    @PostMapping("/otc/publish")
    public Mono<GlobalMessageResponseVo> publish(OtcOfflineOrderVO otcOfflineOrderVO) {
        if (otcOfflineOrderVO.getVolume().compareTo(BigDecimal.ZERO) != 1) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("广告数量非法"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    final PlatUser platUser = platUserService.findById(user.getId());
                    // 发广告前置校验
                    Mono<GlobalMessageResponseVo> preCheckResult = preCheck(user, platUser);
                    if (Objects.nonNull(preCheckResult)) {
                        return preCheckResult;
                    }
                    // 发广告校验
                    Mono<GlobalMessageResponseVo> doCheckResult = doCheck(user, platUser, otcOfflineOrderVO);
                    if (Objects.nonNull(doCheckResult)) {
                        return doCheckResult;
                    }

                    OtcOfflineOrder offlineOrder = new OtcOfflineOrder();
                    BeanUtils.copyProperties(otcOfflineOrderVO, offlineOrder); // 复制，volume,exType,coinid,symbol等
                    offlineOrder.setUserId(user.getId());
                    offlineOrder.setRealName(user.getRealName());
                    offlineOrder.setMobile(user.getMobile());
                    offlineOrder.setCreateDate(LocalDateTime.now());
                    offlineOrder.setUpdateDate(LocalDateTime.now());
                    offlineOrder.setPublishSource(user.getLoginSource());
                    offlineOrder.setTag(user.getTag());

                    otcOfflineOrderService.save(offlineOrder, otcOfflineOrderVO.getSupportBank());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("发布广告成功"));
                });
    }

    /**
     * 广告撤销
     *
     * @return
     */
//    @PostMapping("/otc/gadvert/cancel")
    public Mono<GlobalMessageResponseVo> myAdverCancel(OtcOfflineOrderVO offlineConfirmVO) {
        //查询当天广告撤销次数  如果>=3则广告不能被撤销

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
//                    Mono<GlobalMessageResponseVo> checkCancelResult = checkCancelCount(user); // check每天取消次数，不能超过3次
//                    if (Objects.nonNull(checkCancelResult)) {
//                        return checkCancelResult;
//                    }
                    String userId = user.getId();
                    offlineConfirmVO.setUserId(userId);
                    offlineConfirmVO.setLoginSource(user.getLoginSource());
                    otcOfflineOrderService.advertCancel(offlineConfirmVO);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("取消成功"));
                });
    }

}
