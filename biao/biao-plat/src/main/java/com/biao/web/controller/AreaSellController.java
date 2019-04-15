package com.biao.web.controller;

import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.AreaSellService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 区域售卖
 *
 *  ""ong
 */
@RestController
@RequestMapping("/biao")
public class AreaSellController {

    private static final Logger logger = LoggerFactory.getLogger(AreaSellController.class);

    @Autowired
    private AreaSellService areaSellService;

    /**
     * 查询所有的区域
     *
     * @return
     */
    @GetMapping("/areaSell/findAll")
    public Mono<GlobalMessageResponseVo> findAll() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(areaSellService.findAll()));
    }

    /**
     * 查询所有未出售区域
     *
     * @return
     */
    @GetMapping("/areaSell/findSale")
    public Mono<GlobalMessageResponseVo> findSale() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(areaSellService.findSale()));
    }

    /**
     * 查询所有已售出的区域
     *
     * @return
     */
    @GetMapping("/areaSell/findSold")
    public Mono<GlobalMessageResponseVo> findSold() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(areaSellService.findSold()));
    }

}
