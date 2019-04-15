package com.biao.web.controller.otc;

import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcAppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * otcApp版本管理
 */
@RestController
@RequestMapping("/biao")
public class OtcAppVersionController {

    @Autowired
    private OtcAppVersionService appVersionService;

    /**
     * otcApp版本.
     *
     * @return GlobalMessageResponseVo
     */
    @GetMapping("/otc/appVersion/{type}")
    public Mono<GlobalMessageResponseVo> getVersion(@PathVariable String type) {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(appVersionService.getLastestByType(type)));
    }
}
