package com.biao.reactive.data.mongo.service;

import com.biao.reactive.data.mongo.domain.MatchStream;
import com.biao.reactive.data.mongo.domain.RedisMatchStream;
import com.biao.vo.TradePairVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MatchStreamService.
 *
 *  "".
 */
public interface MatchStreamService {

    /**
     * 根据主区 和交易区币种 获取前多少条.
     *
     * @param coinMain  币种主区 就是使用什么币买
     * @param coinOther 被买入的币种
     * @param limit     rows
     * @return List MatchStream
     */
    List<MatchStream> findTopByCoinMainAndCoinOther(String coinMain, String coinOther, int limit);


    /**
     * 统计某一个交易对 当天每分钟的交易数据.
     *
     * @param coinMain 交易对主区
     * @return List TradePairVO
     */
    List<TradePairVO> statEveryMinForDayTrade(String coinMain);


    /**
     * statByMinDate
     *
     * @param coinMain    coinMain
     * @param coinOther   coinOther
     * @param minDateTime minDateTime
     * @return
     */
    TradePairVO statByMinDate(String coinMain, String coinOther, LocalDateTime minDateTime);


    /**
     * 获取交易对当天的统计信息.
     *
     * @param coinMain  主交易区
     * @param coinOther 被交易币种
     * @param endTime   时间
     * @return List TradePairVO
     */
    List<TradePairVO> acquireToDayStatTradeExpair(String coinMain, String coinOther, LocalDateTime endTime);


    /**
     * 根据主区交易统计.
     *
     * @param coinMain       主区币种
     * @param collectionName 主区交易存储的表(默认所以得币的记录都存一张表)
     * @param startDate      统计时间开始 格式 yyyy-MM-dd (自动格式化为 00:00:00)
     * @param lastDate       统计时间结束 格式 yyyy-MM-dd (自动格式化为 23:59:59)
     * @return List TradePairVO
     */
    List<TradePairVO> findStatisticsTradeByCoinMain(String coinMain, Class<?> collectionName, String startDate, String lastDate);


    /**
     * 根据主区交易统计.
     *
     * @param coinMain       主区币种
     * @param collectionName 主区交易存储的表(默认所以得币的记录都存一张表)
     * @param startDate      统计开始时间
     * @param lastDate       统计结束时间
     * @return List TradePairVO
     */
    List<TradePairVO> findStatisticsTradeByCoinMain(String coinMain, Class<?> collectionName, LocalDateTime startDate, LocalDateTime lastDate);

    /**
     * 根据主区交易统计.
     *
     * @param coinMain       主区币种
     * @param collectionName 主区交易存储的表(默认所以得币的记录都存一张表)
     * @param countDate      统计时间开始 格式 yyyy-MM-dd
     * @return List TradePairVO
     */
    List<TradePairVO> findStatisticsTradeByCoinMain(String coinMain, Class<?> collectionName, String countDate);

    /**
     * 根据主区交易统计当天开始.
     *
     * @param coinMain       主区币种
     * @param collectionName 主区交易存储的表(默认所以得币的记录都存一张表)
     * @return List TradePairVO
     */
    List<TradePairVO> findStatisticsTradeByCoinMain(String coinMain, Class<?> collectionName);

    /**
     * 根据主区查询交易统计,所以记录存一张表.
     *
     * @param coinMain 主区币种
     * @return List TradePairVO
     */
    List<TradePairVO> findStatisticsTradeByCoinMain(String coinMain);


    /**
     * 查询增量或者全量主区的交易对
     *
     * @param coinMain
     * @param collectionName
     * @param startDate      开始时间为空 表示查询全量
     * @param lastDate
     * @return
     */
    List<TradePairVO> findIncrementStatisticsTradeByCoinMain(String coinMain, Class<?> collectionName, LocalDateTime startDate, LocalDateTime lastDate);

    /**
     * 查询增量或者全量主区和被交易区的交易对
     *
     * @param coinMain
     * @param coinOther
     * @param collectionName
     * @param startDate      开始时间为空 表示查询全量
     * @param lastDate
     * @return
     */
    List<RedisMatchStream> findIncrementTrades(String coinMain, String coinOther, Class<?> collectionName, LocalDateTime startDate, LocalDateTime lastDate);

    /**
     * 统计挂单被交易了多少数量.
     *
     * @param orderNo 挂单号
     * @return sum volume
     */
    BigDecimal sumTotalVolumeByOrderNo(String orderNo);

    /**
     * 根据挂单号获取流水记录.
     *
     * @param orderNo 挂单号
     * @return List MatchStream
     */
    List<MatchStream> listByOrderNo(String orderNo);
}
