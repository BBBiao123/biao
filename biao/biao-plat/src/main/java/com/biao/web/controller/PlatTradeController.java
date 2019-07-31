package com.biao.web.controller;

import com.biao.coin.CoinMainService;
import com.biao.handler.KlineDataHandler;
import com.biao.handler.PlatDataHandler;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.vo.TradePairVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 首页,币币界面获取交易对数据.
 *
 *  ""
 */
@RestController
@RequestMapping("/biao")
public class PlatTradeController {

    private final PlatDataHandler platDataHandler;

    private final KlineDataHandler klineDataHandler;

    @Autowired
    private CoinMainService coinMainService;

    @Autowired
    public PlatTradeController(final PlatDataHandler platDataHandler, KlineDataHandler klineDataHandler) {
        this.platDataHandler = platDataHandler;
        this.klineDataHandler = klineDataHandler;
    }


    /**
     * 获取btc eth 对应usdt的价格.
     *
     * @return GlobalMessageResponseVo
     */
    @GetMapping("/index/lastPrice")
    public Mono<GlobalMessageResponseVo> lastPrice() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(platDataHandler.buildLastPrice()));
    }


    /**
     * 获取btc eth 对应usdt的价格.
     *
     * @return GlobalMessageResponseVo
     */
    @GetMapping("/index/mainCoinCnb")
    public Mono<GlobalMessageResponseVo> mainCoinCnb() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(platDataHandler.acquireAllMainCnb()));
    }

    /**
     * 币币界面获取交易对数据.
     *
     * @return GlobalMessageResponseVo
     */
    @GetMapping("/index/allTradeExpair")
    public Mono<GlobalMessageResponseVo> allTradeExpair() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(platDataHandler.buildAllTradePair()))
                .subscribeOn(Schedulers.parallel());
    }


    /**
     * 币币界面获取交易对数据.
     *
     * @return GlobalMessageResponseVo
     */
    @GetMapping("/index/coinMainList")
    public Mono<GlobalMessageResponseVo> coinMainList() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(coinMainService.getList()));

    }


    /*  *//**
     * 币币界面获取k线
     *
     * @param coinMain  主交易区
     * @param coinOther 被交易币种
     * @param time      时间
     * @return GlobalMessageResponseVo
     *//*
    @GetMapping("/index/kline")
    public Mono<GlobalMessageResponseVo> kline(@RequestParam("coinMain") final String coinMain,
                                               @RequestParam("coinOther") final String coinOther,
                                               @RequestParam("time") final String time) {
        return Mono.just(
                GlobalMessageResponseVo.newSuccessInstance(
                        klineDataHandler.buildResult(coinOther, coinMain, time)))
                .subscribeOn(Schedulers.parallel());
    }*/


    /**
     * 币币界面获取交易流水.
     *
     * @param coinMain  主交易区
     * @param coinOther 被交易币种
     * @return Mono GlobalMessageResponseVo
     */
    @GetMapping("/index/findMatchStream")
    public Mono<GlobalMessageResponseVo> findMatchStream(@RequestParam("coinMain") final String coinMain,
                                                         @RequestParam("coinOther") final String coinOther) {
        return Mono.just(GlobalMessageResponseVo
                .newSuccessInstance(platDataHandler.buildMatchStream(coinMain, coinOther)))
                .subscribeOn(Schedulers.parallel());
    }

    /**
     * 币币界面小数点位数合并.
     *
     * @param coinMain  主交易区
     * @param coinOther 被交易币种
     * @param type      0 买 1 卖 2 全部
     * @param length    小数点位数
     * @return GlobalMessageResponseVo
     */
    @GetMapping("/index/merge")
    public Mono<GlobalMessageResponseVo> merge(@RequestParam("coinMain") final String coinMain,
                                               @RequestParam("coinOther") final String coinOther,
                                               @RequestParam("type") final int type,
                                               @RequestParam("length") final int length) {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(platDataHandler.merge(coinMain, coinOther, type, length)));
    }

    @GetMapping("/index/allTrades")
    public Mono<Map<String,Object>> allTrades() {

        Map<String,Object> tradeMap=new HashMap<>();
        Date date=new Date();
        tradeMap.put("date", date.getTime());
        tradeMap.put("ticker", platDataHandler.buildAllTrades());
        return Mono.just(tradeMap);
    }
}