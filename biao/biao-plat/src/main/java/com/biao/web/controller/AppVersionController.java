package com.biao.web.controller;

import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * App版本管理
 *
 *  ""zj
 */
@RestController
@RequestMapping("/biao")
public class AppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    /**
     * App版本.
     *
     * @return GlobalMessageResponseVo
     */
    @GetMapping("/index/appVersion/{type}")
    public Mono<GlobalMessageResponseVo> getVersion(@PathVariable String type) {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(appVersionService.getLastestByType(type)));
    }

}
