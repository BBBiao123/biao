package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.constant.SercurityConstant;
import com.biao.entity.PlatUser;
import com.biao.entity.UserBank;
import com.biao.enums.UserCardStatusEnum;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.PlatUserService;
import com.biao.service.UserBankService;
import com.biao.util.JsonUtils;
import com.biao.vo.AlipayVO;
import com.biao.vo.BankVO;
import com.biao.vo.UserBankVO;
import com.biao.vo.WechatVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 用户支付设置相关操作
 */
@RestController
@RequestMapping("/biao")
public class UserBankController {

    @Autowired
    private UserBankService userBankService;
    @Autowired
    private PlatUserService platUserService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    /**
     * 获取用户银行信息
     *
     * @param
     * @return
     */
    @GetMapping("/offline/bank/get")
    public Mono<GlobalMessageResponseVo> get() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(userBankService.findByUserId(userId)));
                });
    }

    /**
     * 绑定银行卡
     *
     * @param userBankVO
     * @return
     */
    @PostMapping("/offline/bank/bind")
    public Mono<GlobalMessageResponseVo> bind(UserBankVO userBankVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                	if (!UserCardStatusEnum.authRealName(user.getCardStatus(),user.getCountryCode())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请进行身份认证!"));
                    }
                    if (StringUtils.isBlank(user.getRealName())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请退出重新登录"));
                    }
                    //增加绑定验证
                    if (StringUtils.isBlank(userBankVO.getRealName()) || !(userBankVO.getRealName().trim().equals(user.getRealName().trim()))) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "与身份证姓名不一致"));
                    }
                    String userId = user.getId();
                    UserBank userBank = new UserBank();
                    BeanUtils.copyProperties(userBankVO, userBank);
                    userBank.setUserId(userId);
                    userBankService.save(userBank);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("成功"));
                });
    }

    /**
     * 清空银行卡
     *
     * @param
     * @return
     */
    @PostMapping("/offline/bank/clean")
    public Mono<GlobalMessageResponseVo> clean() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    userBankService.deleteByUserId(userId);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("成功"));
                });
    }

    @RequestMapping("/offline/bank/list")
    public Mono<GlobalMessageResponseVo> list() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(userBankService.findAll(user.getId())));
                });
    }


    /**
     * 绑定支付宝账号
     *
     * @param alipayVO
     * @return
     */
    @PostMapping("/offline/alipay/bind")
    public Mono<GlobalMessageResponseVo> alipaybind(AlipayVO alipayVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    if (!UserCardStatusEnum.authRealName(user.getCardStatus(),user.getCountryCode())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请进行身份认证!"));
                    }
                    if (!alipayVO.getRealName().equals(user.getRealName())) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("与身份证姓名不一致!"));
                    }
                    String userId = user.getId();
                    platUserService.updateAlipay(userId, alipayVO.getAlipayNo(), alipayVO.getAlipayQrcodeId());
                    user.setAlipayNo(alipayVO.getAlipayNo());
                    user.setAlipayQrcodeId(alipayVO.getAlipayQrcodeId());
                    //更新redis数据
                    stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("ok"));
                });
    }

    /**
     * 清空支付宝账号
     *
     * @param
     * @return
     */
    @PostMapping("/offline/alipay/clean")
    public Mono<GlobalMessageResponseVo> alipayclean() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    platUserService.updateAlipay(userId, null, null);
                    user.setAlipayNo(null);
                    user.setAlipayQrcodeId(null);
                    //更新redis数据
                    stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("ok"));
                });
    }

    @PostMapping("/offline/alipay/get")
    public Mono<GlobalMessageResponseVo> getAlipayInfo() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    AlipayVO alipayVO = new AlipayVO();
                    PlatUser platUser = platUserService.findById(user.getId());
                    BeanUtils.copyProperties(platUser, alipayVO);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(alipayVO));
                });
    }

    /**
     * 绑定微信账号
     *
     * @param wechatVO
     * @return
     */
    @PostMapping("/offline/wechatpay/bind")
    public Mono<GlobalMessageResponseVo> wechatbind(WechatVO wechatVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                	if (!UserCardStatusEnum.authRealName(user.getCardStatus(),user.getCountryCode())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请进行身份认证!"));
                    }
                    if (!wechatVO.getRealName().equals(user.getRealName())) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("与身份证姓名不一致!"));
                    }
                    String userId = user.getId();
                    platUserService.updateWechat(userId, wechatVO.getWechatNo(), wechatVO.getWechatQrcodeId());
                    user.setWechatNo(wechatVO.getWechatNo());
                    user.setWechatQrcodeId(wechatVO.getWechatQrcodeId());
                    //更新redis数据
                    stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("ok"));
                });

    }

    /**
     * 清空微信账号
     *
     * @param
     * @return
     */
    @PostMapping("/offline/wechatpay/clean")
    public Mono<GlobalMessageResponseVo> wechatclean() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    platUserService.updateWechat(userId, null, null);

                    user.setWechatNo(null);
                    user.setWechatQrcodeId(null);
                    //更新redis数据
                    stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("ok"));
                });

    }

    @PostMapping("/offline/wechatpay/get")
    public Mono<GlobalMessageResponseVo> getWechatInfo() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    WechatVO wechatVO = new WechatVO();
                    PlatUser platUser = platUserService.findById(user.getId());
                    BeanUtils.copyProperties(platUser, wechatVO);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(wechatVO));
                });
    }

    /**
     * 获取他人银行卡信息
     *
     * @param otherUserId
     * @return
     */
    @GetMapping("/offline/bankInfo/{otherUserId}")
    public Mono<GlobalMessageResponseVo> bankInfo(@PathVariable("otherUserId") String otherUserId) {
        Optional<UserBank> optional = userBankService.findByUserId(otherUserId);
        PlatUser platUser = platUserService.findById(otherUserId);
        BankVO bankVO = new BankVO();
        BeanUtils.copyProperties(optional.orElse(new UserBank()), bankVO);
        optional.ifPresent(userBank -> {
            bankVO.setBankUserName(userBank.getRealName());
        });
        BeanUtils.copyProperties(platUser, bankVO);
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(bankVO));

    }

}
