package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.EasemobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/biao")
public class EasemobController {

    @Autowired
    private EasemobService easemobService;

    @PostMapping("/easemob/register")
    public Mono<GlobalMessageResponseVo> register() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    easemobService.register(e.getId(), e.getCsUsername());
                    return GlobalMessageResponseVo.newSuccessInstance("注册成功");
                });
    }
}
