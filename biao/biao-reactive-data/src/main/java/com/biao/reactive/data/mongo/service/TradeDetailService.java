package com.biao.reactive.data.mongo.service;

import com.biao.pojo.ResponsePage;
import com.biao.query.UserTradeQuery;
import com.biao.vo.ReportTradeFreeVO;
import com.biao.vo.TradeExPairDayAvgVO;
import com.biao.vo.UserTradeVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TradeDetailService.
 *
 *  ""
 */
public interface TradeDetailService {


    /**
     * 根据查询条件获取用户交易对详情.
     *
     * @param userTradeQuery 查询条件
     * @return List UserTradeVO
     */
    ResponsePage<UserTradeVO> findByPage(UserTradeQuery userTradeQuery);


    /**
     * 根据订单号/id 获取交易详情.
     *
     * @param orderNo 订单id
     * @return List UserTradeVO
     */
    List<UserTradeVO> findByOrderNo(String orderNo);


    /**
     * 根据交易号，订单号获取唯一条详情.
     *
     * @param tradeNo 交易号
     * @param orderNo 挂单号
     * @return UserTradeVO
     */
    UserTradeVO findByTradeNoAndOrderNo(String tradeNo, String orderNo);

    /**
     * 根据交易号，type获取唯一条详情.
     *
     * @param tradeNo 交易号
     * @param type    type
     * @return UserTradeVO
     */
    UserTradeVO findByTradeNoAndType(String tradeNo, Integer type);

    /**
     * 根据交易类型统计交易手续费
     *
     * @param type 0:买入  1:卖出
     * @return
     */
    List<ReportTradeFreeVO> countTradeFree(Integer type, final LocalDateTime startDate, final LocalDateTime lastDate);

    /**
     * 根据日期获取交易详情.
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return List UserTradeVO
     */
    List<UserTradeVO> findByDate(String startDate, String endDate);

    /**
     * 根据日期获取交易详情.
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return List UserTradeVO
     */
    List<UserTradeVO> findByDateAndCoinMain(String startDate, String endDate, String coinMain);

    /**
     * 根据某日币币对平均价
     *
     * @param date      日期
     * @param coinOne   主区
     * @param coinOther
     * @return TradeExPairDayAvgVO
     */
    TradeExPairDayAvgVO findDayAverageByExPair(String date, String coinOne, String coinOther);

}
