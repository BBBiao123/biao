package com.biao.web.controller;


import com.biao.entity.Coin;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 币种资料查看
 */
@RestController
@RequestMapping("/biao")
public class CoinController {

    @Autowired
    private CoinService coinService;

    /**
     * 币种id
     *
     * @param
     * @return
     */
    @GetMapping("/coin/detail/{symbol}")
    @ResponseBody
    public Mono<GlobalMessageResponseVo> detail(@PathVariable("symbol") String symbol) {
        Coin coin = coinService.findByName(symbol);
        Map<String, Object> data = new HashMap<>();
        data.put("remarks", coin.getRemarks());
        data.put("icoPrice", coin.getIcoPrice());
        data.put("tokenVolume", coin.getTokenVolume());
        data.put("whitepaperUrl", coin.getWhitepaperUrl());
        data.put("domain", coin.getDomain());
        data.put("fullName", coin.getFullName());
        data.put("name", coin.getName());
        data.put("circulateVolume", coin.getCirculateVolume());
        data.put("coinType", coin.getCoinType());
        data.put("iconId", coin.getIconId());
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(data));
    }

    /**
     * 币种列表
     *
     * @param
     * @return
     */
    @GetMapping("/coin/list")
    @ResponseBody
    public Mono<GlobalMessageResponseVo> list() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(coinService.findAll()));
    }

}
