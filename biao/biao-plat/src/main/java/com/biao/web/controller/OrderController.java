package com.biao.web.controller;

import com.biao.query.UserTradeQuery;
import com.biao.service.OrderService;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 一些订单的操作；
 *
 *
 */
@RestController
@RequestMapping("/biao")
public class OrderController {


    /**
     * 订单处理的service;
     */
    @Autowired
    private OrderService orderService;

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "userId", notNull = true, errMsg = "用户id不允许为空！"),
            @ValidateFiled(index = 0, filedName = "coinMain", notNull = true, errMsg = "交易区不允许为空！"),
            @ValidateFiled(index = 0, filedName = "coinOther", notNull = true, errMsg = "交易区对不允许为空！"),
            @ValidateFiled(index = 0, filedName = "status", notNull = true, errMsg = "状态的处理！")
    })
    @RequestMapping("/order/findOrderProposeList")
    public <T> Mono<T> findOrderProposeList(UserTradeQuery userTradeQuery) {
        return orderService.findOrderProposeList(userTradeQuery);
    }
}
