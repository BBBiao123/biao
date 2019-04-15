package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.PlatUser;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.RequestQuery;
import com.biao.service.LuckyDrawService;
import com.biao.service.PlatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 抽奖活动
 */

@RestController
@RequestMapping("/biao")
public class LuckyDrawController {

    @Autowired
    private LuckyDrawService luckyDrawService;

    @Autowired
    private PlatUserService platUserService;

    @PostMapping("/lucky/list")
    public Mono<GlobalMessageResponseVo> list(RequestQuery requestQuery) {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(luckyDrawService.findPage(requestQuery)));
    }

    @PostMapping("/lucky/in")
    public Mono<GlobalMessageResponseVo> winnersList(RequestQuery requestQuery) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    PlatUser platUser = platUserService.findById(user.getId());
                    luckyDrawService.in(platUser);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("参与成功！"));
                });
    }

    @PostMapping("/lucky/myList")
    public Mono<GlobalMessageResponseVo> myList(RequestQuery requestQuery) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    PlatUser platUser = platUserService.findById(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(luckyDrawService.findMyPage(requestQuery, platUser)));
                });
    }


}
