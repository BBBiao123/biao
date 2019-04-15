package com.biao.web.controller.lottery;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.lottery.LotteryVO;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.register.UserRegisterLotteryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * UserRegisterLotteryController.
 *
 *  ""
 */
@RestController
@RequestMapping("/biao")
public class UserRegisterLotteryController {

    private final UserRegisterLotteryService userRegisterLotteryService;

    @Autowired
    public UserRegisterLotteryController(UserRegisterLotteryService userRegisterLotteryService) {
        this.userRegisterLotteryService = userRegisterLotteryService;
    }

    /**
     * registerLotteryCheck.
     *
     * @return GlobalMessageResponseVo
     */
    @PostMapping("/lottery/check")
    public Mono<GlobalMessageResponseVo> registerLotteryCheck() {

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    final Boolean success = userRegisterLotteryService.checkLottery(user.getId());
                    if (success) {
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("用户符合抽奖条件"));
                    } else {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户不符合抽奖条件"));
                    }
                });


    }

    @RequestMapping("/lottery/do/{source}")
    public Mono<GlobalMessageResponseVo> lottery(@PathVariable("source") String source) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    final LotteryVO lotteryVO = userRegisterLotteryService.lottery(user.getId(), source);
                    if (Objects.nonNull(lotteryVO) && StringUtils.isNoneBlank(lotteryVO.getCoinSymbol())) {
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(lotteryVO));
                    } else {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance(lotteryVO.getMsg()));
                    }
                });
    }

}