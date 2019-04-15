package com.biao.web.controller.otc;

import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcOfflineCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * OTC法币币种
 *
 *  ""azi
 */
@RestController
@RequestMapping("/biao")
public class OtcOfflineCurrencyController {

    @Autowired
    private OtcOfflineCurrencyService otcOfflineCurrencyService;

    /**
     * OTC支持的所有币种
     *
     * @return
     */
    @GetMapping("/otc/currency/list")
    public Mono<GlobalMessageResponseVo> list() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcOfflineCurrencyService.findAll()));
    }
}
