package com.biao.service;


import com.biao.pojo.BatchCancelTradeDTO;
import com.biao.pojo.TradeVo;
import reactor.core.publisher.Mono;

/**
 * The interface Trade service.
 *
 *
 * @date 2018 /4/6 交易相关的实现处理;
 */
public interface TradeService {
    /**
     * 获取一个ID号；
     *
     * @param <T> the type parameter
     * @return order no
     */
    <T> Mono<T> getOrderNo();

    /**
     * 买入交易
     * 1. 所有参数不允许为空；
     *
     * @param <T>     返回数据；
     * @param tradeVo 数据对象；
     * @return 返回数据体操作结果 ；
     */
    <T> Mono<T> buyIn(TradeVo tradeVo);

    /**
     * 卖出交易
     * 1.所有参数不允许为空
     *
     * @param <T>     返回数据；
     * @param tradeVo 数据对象；
     * @return 返回数据体操作结果 ；
     */
    <T> Mono<T> sellOut(TradeVo tradeVo);

    /**
     * 取消交易；
     *
     * @param <T>     返回数据；
     * @param userId  the user id
     * @param orderNo orderNo;
     * @return 返回数据操作结果 ；
     */
    <T> Mono<T> cancelTrade(String userId, String orderNo);

    /**
     * 批量取消.一个异步实现的接口.
     * 内部使用JVM队列，进行交易异步的取消.
     *
     * @param <T> 类型.
     * @param dto 请求的dto.
     * @return 只要数据提交的队列成功的结果. mono
     */
    <T> Mono<T>  batchCancelTrade(BatchCancelTradeDTO dto);
}
