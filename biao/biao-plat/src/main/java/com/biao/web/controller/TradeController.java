package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.exception.PlatException;
import com.biao.pojo.BatchCancelTradeDTO;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.TradeVo;
import com.biao.service.TradeService;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 交易买入与买出的处理;
 *
 *  ""
 */
@RestController
@RequestMapping("/biao")
public class TradeController {
    /**
     * {
     * "userId":"123",
     * "tradeNo":"123",
     * "count":1.2,
     * "price":1.3,
     * "currencyId":1,
     * "tradeCurrency":1
     * <p>
     * }
     */
    @Autowired
    private TradeService tradeService;

    /**
     * 买入;
     *
     * @param tradeVo 数据对象;
     * @return 操作结果 ;
     */
    @RequestMapping("/trade/buyIn")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "userId", notNull = true, errMsg = "用户id不允许为空！"),
            @ValidateFiled(index = 0, filedName = "coinMain", notNull = true, errMsg = "交易区不允许为空！"),
            @ValidateFiled(index = 0, filedName = "coinOther", notNull = true, errMsg = "交易区对不允许为空！"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "数量不允许为空！"),
            @ValidateFiled(index = 0, filedName = "price", notNull = true, errMsg = "价格不允许为空！"),
            @ValidateFiled(index = 0, filedName = "orderNo", notNull = true, errMsg = "订单号不允许为空！")
    })
    public Mono<Object> buyIn(TradeVo tradeVo) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    final boolean lockTrade = user.isLockTrade();
                    if (lockTrade) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户被锁定不能交易"));
                    }
                    tradeVo.setUserId(user.getId());
                    return tradeService.buyIn(tradeVo);
                });
    }

    /**
     * 卖出
     *
     * @param tradeVo tradeVo数据对象
     * @return 操作结果 ;
     */
    @RequestMapping("/trade/sellOut")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "userId", notNull = true, errMsg = "用户id不允许为空！"),
            @ValidateFiled(index = 0, filedName = "coinMain", notNull = true, errMsg = "交易区不允许为空！"),
            @ValidateFiled(index = 0, filedName = "coinOther", notNull = true, errMsg = "交易区对不允许为空！"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "数量不允许为空！"),
            @ValidateFiled(index = 0, filedName = "price", notNull = true, errMsg = "价格不允许为空！"),
            @ValidateFiled(index = 0, filedName = "orderNo", notNull = true, errMsg = "订单号不允许为空！")
    })
    public Mono<Object> sellOut(TradeVo tradeVo) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    final boolean lockTrade = user.isLockTrade();
                    if (lockTrade) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户被锁定不能交易"));
                    }
                    tradeVo.setUserId(user.getId());
                    return tradeService.sellOut(tradeVo);
                });
    }

    /**
     * 批量取消交易处理.
     *
     * @param dto 批量取消的dto请滶
     * @return 操作结果 ;
     */
    @RequestMapping("/trade/batchCancelTrade")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "userId", notNull = true, errMsg = "用户id不允许为空！"),
            @ValidateFiled(index = 0, filedName = "coinMain", notNull = true, errMsg = "交易区不允许为空！"),
            @ValidateFiled(index = 0, filedName = "coinOther", notNull = true, errMsg = "交易区对不允许为空！"),
    })
    public Mono<Object> batchCancelTrade(BatchCancelTradeDTO dto) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    final boolean lockTrade = user.isLockTrade();
                    if (lockTrade) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户被锁定不能交易"));
                    }
                    dto.setUserId(user.getId());
                    return tradeService.batchCancelTrade(dto);
                });
    }

   /* *//**
     * 批量取消交易处理.
     *
     * @param dto 批量取消的dto请滶
     * @return 操作结果 ;
     *//*
    @RequestMapping("/trade/batchCancelTradeAbc")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "userId", notNull = true, errMsg = "用户id不允许为空！"),
    })
    public Mono<Object> batchCancelTradeAbc(BatchCancelTradeDTO dto) {
        return tradeService.batchCancelTrade(dto);
    }
*/
    /**
     * 获取订单ID
     *
     * @param <T> the type parameter
     * @return 操作结果 ;
     */
    @RequestMapping("/trade/getOrderNo")
    public <T> Mono<T> getOrderNo() {
        return tradeService.getOrderNo();
    }

    /**
     * 取消交易；
     *
     * @param orderNo the order no
     * @return 操作结果 ；
     */
    @GetMapping("/trade/cancelTrade/{orderNo}")
    public Mono<Object> cancelTrade(@PathVariable("orderNo") String orderNo) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    final boolean lockTrade = user.isLockTrade();
                    if (lockTrade) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户被锁定不能交易"));
                    }
                    try {
                        return tradeService.cancelTrade(user.getId(), orderNo);
                    } catch (PlatException ex) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance(ex.getMsg()));
                    }
                });
    }
}
