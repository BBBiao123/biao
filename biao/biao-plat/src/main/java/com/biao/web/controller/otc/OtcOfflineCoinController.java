package com.biao.web.controller.otc;

import com.biao.enums.AppSourceTypeEnum;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcOfflineCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * OTC可卖币种列表
 *
 *  ""f
 */
@RestController
@RequestMapping("/biao")
public class OtcOfflineCoinController {

    @Autowired
    private OtcOfflineCoinService offlineCoinService;

    @GetMapping("/otc/coin/list")
    public Mono<GlobalMessageResponseVo> findAll() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineCoinService.findAllByPublishSource(AppSourceTypeEnum.OTC.getCode())));
    }

}
