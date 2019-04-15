package com.biao.service;

import com.biao.entity.Order;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.ResponsePage;
import com.biao.pojo.TradeDto;
import com.biao.query.UserTradeQuery;
import com.biao.vo.UserTradeVO;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * The interface Order service.
 */
public interface OrderService {


    /**
     * Find by id order.
     *
     * @param id the id
     * @return the order
     */
    Order findById(String id);

    /**
     * Save long.
     *
     * @param order the order
     * @return the long
     */
    long save(Order order);

    /**
     * Update by id.
     *
     * @param order the order
     */
    void updateById(Order order);

    /**
     * 修改订单号信息；
     *
     * @param e 订单列表；
     * @return order ;
     */
    GlobalMessageResponseVo updateResultOrders(Order e);

    /**
     * 修改状态
     *  大于0表示成功.
     * @param orderNo the order no
     * @param status  the status
     * @return global message response vo
     */
    long updateStatus(String orderNo, Integer status);

    /**
     * 获取用户当前委托的挂单
     *
     * @param userTradeQuery 前台查询条件
     * @return ResponsePage<UserTradeVO>   response page
     */
    ResponsePage<UserTradeVO> findByPage(UserTradeQuery userTradeQuery);

    /**
     * 查询条件用户的已经撮合完成的数据，包括部分完成的数据；
     *
     * @param <T>            the type parameter
     * @param userTradeQuery 用户数据；                       {@link UserTradeQuery#status}在这个方法的处理中是可以找到的。                       0：表示查询未成功，与部分成功；                       1：表示查询 查询所有；
     * @return 数据 ；
     * @see UserTradeQuery
     */
    <T> Mono<T> findOrderProposeList(UserTradeQuery userTradeQuery);

    /**
     * Find top 7 list.
     *
     * @param type the type
     * @return the list
     */
    List<TradeDto> findTop7(Integer type);


}
