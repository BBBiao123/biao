package com.biao.web.controller;

import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Plat系统配置
 *
 *  ""zj
 */
@RestController
@RequestMapping("/biao")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * Plat系统配置.
     *
     * @return GlobalMessageResponseVo
     */
    @GetMapping("/index/sys/config")
    public Mono<GlobalMessageResponseVo> getOne() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(sysConfigService.getOne()));
    }

}
