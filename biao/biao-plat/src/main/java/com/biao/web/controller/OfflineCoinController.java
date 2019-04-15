package com.biao.web.controller;

import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.OfflineCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * c2c可卖币种列表
 *
 *  ""azi
 */
@RestController
@RequestMapping("/biao")
public class OfflineCoinController {

    @Autowired
    private OfflineCoinService offlineCoinService;

    @GetMapping("/offline/coin/list")
    public Mono<GlobalMessageResponseVo> findAll() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineCoinService.findAll()));
    }
}
