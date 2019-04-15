package com.biao.controller;

import com.biao.kline.cache.KlineCacheDataList;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.vo.KlineResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 首页,币币界面获取交易对数据.
 *
 *  ""
 */
@RestController
@RequestMapping("/biao")
public class KlineController {


    /**
     * 币币界面获取k线.
     *
     * @param coinMain  主交易区
     * @param coinOther 被交易币种
     * @param time      时间
     * @return GlobalMessageResponseVo
     */
    @GetMapping("/index/kline")
    public Mono<GlobalMessageResponseVo> kline(@RequestParam("coinMain") final String coinMain,
                                               @RequestParam("coinOther") final String coinOther,
                                               @RequestParam("time") final String time) {
        return Mono.just(
                GlobalMessageResponseVo.newSuccessInstance(
                        buildResult(coinMain, coinOther, time)))
                .subscribeOn(Schedulers.parallel());
    }


    private KlineResult buildResult(final String coinMain, final String coinOther, final String time) {
        KlineResult klineResult = new KlineResult();
        klineResult.setType("kline");
        klineResult.setCode(0);
        klineResult.setData(KlineCacheDataList.readKlineVO(coinMain, coinOther, time));
        return klineResult;
    }

}