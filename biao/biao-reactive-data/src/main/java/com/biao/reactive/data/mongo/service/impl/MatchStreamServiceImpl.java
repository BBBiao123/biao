package com.biao.reactive.data.mongo.service.impl;

import com.biao.reactive.data.mongo.domain.MatchStream;
import com.biao.reactive.data.mongo.domain.RedisMatchStream;
import com.biao.reactive.data.mongo.service.MatchStreamService;
import com.biao.util.DateUtils;
import com.biao.vo.OrderNoTotalVO;
import com.biao.vo.TradePairVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * MatchStreamServiceImpl.
 *
 *  ""(Myth)
 */
@Service("matchStreamService")
public class MatchStreamServiceImpl implements MatchStreamService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据主区 和交易区币种 分页获取.
     *
     * @param coinMain  币种主区 就是使用什么币买
     * @param coinOther 被买入的币种
     * @param limit     rows
     * @return List MatchStream
     */
    @Override
    public List<MatchStream> findTopByCoinMainAndCoinOther(final String coinMain, final String coinOther, final int limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "tradeTime");
        Query query = new Query();
        if (StringUtils.isNoneBlank(coinMain)) {
            query.addCriteria(new Criteria("coinMain").is(coinMain));
        }
        if (StringUtils.isNoneBlank(coinOther)) {
            query.addCriteria(new Criteria("coinOther").is(coinOther));
        }
        query.with(sort).limit(limit);
        return mongoTemplate.find(query, MatchStream.class);
    }

    /**
     * 统计某一个交易对 当天每分钟的交易数据.
     *
     * @param coinMain 交易对主区
     * @return List TradePairVO
     */
    @Override
    public List<TradePairVO> statEveryMinForDayTrade(final String coinMain) {
        LocalDateTime start = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(23, 59, 59));
        Criteria operator = Criteria.where("coinMain")
                .is(coinMain)
                .andOperator(Criteria.where("tradeTime").gte(start).lte(end));
        MatchOperation matchOperation = Aggregation.match(operator);
        GroupOperation groupOperation = buildGroup();
        //获取的字段
        ProjectionOperation projectionOperation =
                Aggregation.project("latestPrice", "firstPrice", "highestPrice", "lowerPrice", "dayCount", "coinOther", "coinMain", "minuteTime");
        //构建查询
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation);

        AggregationResults<TradePairVO> tradePairVOFlux =
                mongoTemplate.aggregate(aggregation, MatchStream.class, TradePairVO.class);
        //返回结果
        return tradePairVOFlux.getMappedResults();
    }

    /**
     * statByMinDate
     *
     * @param coinMain    coinMain
     * @param coinOther   coinOther
     * @param minDateTime minDateTime
     * @return
     */
    @Override
    public TradePairVO statByMinDate(String coinMain, String coinOther, LocalDateTime minDateTime) {
        LocalTime startTime = LocalTime.of(minDateTime.getHour(), minDateTime.getMinute(), 0);
        LocalDateTime start = LocalDateTime.of(minDateTime.toLocalDate(), startTime);
        LocalTime endTime = LocalTime.of(minDateTime.getHour(), minDateTime.getMinute(), 59);
        LocalDateTime end = LocalDateTime.of(minDateTime.toLocalDate(), endTime);
        Criteria operator = Criteria.where("coinMain")
                .is(coinMain)
                .and("coinOther").is(coinOther)
                .andOperator(Criteria.where("tradeTime").gte(start).lte(end));
        MatchOperation matchOperation = Aggregation.match(operator);
        GroupOperation groupOperation = buildGroup();
        //获取的字段
        ProjectionOperation projectionOperation =
                Aggregation.project("latestPrice", "firstPrice", "highestPrice", "lowerPrice", "dayCount", "coinOther", "coinMain", "minuteTime");
        //构建查询
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation);

        AggregationResults<TradePairVO> tradePairVOFlux =
                mongoTemplate.aggregate(aggregation, MatchStream.class, TradePairVO.class);
        final List<TradePairVO> tradePairVOS = tradePairVOFlux.getMappedResults();
        if (CollectionUtils.isNotEmpty(tradePairVOS)) {
            return tradePairVOS.get(0);
        }
        //返回结果
        return null;
    }

    private GroupOperation buildGroup() {
        return Aggregation.group("coinOther", "coinMain", "minuteTime")
                .sum("volume").as("dayCount")
                .max("price").as("highestPrice")
                .min("price").as("lowerPrice")
                .last("price").as("latestPrice")
                .first("price").as("firstPrice");
    }

    @Override
    public List<TradePairVO> acquireToDayStatTradeExpair(final String coinMain, final String coinOther, final LocalDateTime endTime) {
        LocalDateTime start = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(0, 0, 0));
        Criteria operator = Criteria.where("coinMain").is(coinMain)
                .and("coinOther").is(coinOther)
                .andOperator(Criteria.where("tradeTime").gte(start).lte(endTime));
        MatchOperation matchOperation = Aggregation.match(operator);
        GroupOperation groupOperation = buildGroup();
        //获取的字段
        ProjectionOperation projectionOperation =
                Aggregation.project("latestPrice", "firstPrice", "highestPrice", "lowerPrice", "dayCount", "coinOther", "coinMain", "minuteTime");
        //构建查询
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation);

        AggregationResults<TradePairVO> tradePairVOFlux =
                mongoTemplate.aggregate(aggregation, MatchStream.class, TradePairVO.class);
        //返回结果
        return tradePairVOFlux.getMappedResults();
    }


    /**
     * 统计挂单被交易了多少数量.
     *
     * @param orderNo 挂单号
     * @return sum volume
     */
    @Override
    public BigDecimal sumTotalVolumeByOrderNo(final String orderNo) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("orderNo").is(orderNo)),
                Aggregation.group("coinOther", "coinMain").sum("volume").as("totalVolume"),
                Aggregation.project("totalVolume")
        );
        final AggregationResults<OrderNoTotalVO> aggregate = mongoTemplate.aggregate(agg, MatchStream.class, OrderNoTotalVO.class);
        return Objects.requireNonNull(aggregate.getMappedResults()).get(0).getTotalVolume();
    }

    /**
     * 根据挂单号获取流水记录.
     *
     * @param orderNo 挂单号
     * @return List MatchStream
     */
    @Override
    public List<MatchStream> listByOrderNo(final String orderNo) {
        Query query = new Query();
        if (StringUtils.isNoneBlank(orderNo)) {
            query.addCriteria(new Criteria("orderNo").is(orderNo));
        }
        return mongoTemplate.find(query, MatchStream.class);
    }

    @Override
    public List<TradePairVO> findStatisticsTradeByCoinMain(final String coinMain) {
        return findStatisticsTradeByCoinMain(coinMain, MatchStream.class, null, (String) null);
    }

    @Override
    public List<TradePairVO> findStatisticsTradeByCoinMain(final String coinMain, final Class<?> collectionName) {
        return findStatisticsTradeByCoinMain(coinMain, collectionName, null, (String) null);
    }

    @Override
    public List<TradePairVO> findStatisticsTradeByCoinMain(final String coinMain, final Class<?> collectionName, final String countDate) {
        return findStatisticsTradeByCoinMain(coinMain, collectionName, countDate, null);
    }

    @Override
    public List<TradePairVO> findStatisticsTradeByCoinMain(final String coinMain, final Class<?> collectionName, final String startDate, final String lastDate) {
        Aggregation aggregation = createAggregation(coinMain, startDate, lastDate);
        AggregationResults<TradePairVO> tradePairVOsFlux = mongoTemplate.aggregate(aggregation, collectionName, TradePairVO.class);
        //返回结果
        return tradePairVOsFlux.getMappedResults();
    }

    @Override
    public List<TradePairVO> findStatisticsTradeByCoinMain(final String coinMain, final Class<?> collectionName, final LocalDateTime startDate, final LocalDateTime lastDate) {
        Aggregation aggregation = createAggregation(coinMain, startDate, lastDate);
        AggregationResults<TradePairVO> tradePairVOsFlux = mongoTemplate.aggregate(aggregation, collectionName, TradePairVO.class);
        //返回结果
        return tradePairVOsFlux.getMappedResults();
    }


    @Override
    public List<TradePairVO> findIncrementStatisticsTradeByCoinMain(String coinMain, Class<?> collectionName,
                                                                    LocalDateTime startDate, LocalDateTime lastDate) {
        Aggregation aggregation = createIncrementAggregation(coinMain, null, startDate, lastDate);
        AggregationResults<TradePairVO> tradePairVOsFlux = mongoTemplate.aggregate(aggregation, collectionName, TradePairVO.class);
        //返回结果
        return tradePairVOsFlux.getMappedResults();
    }


    private Aggregation createIncrementAggregation(final String coinMain, String coinOther, final LocalDateTime startDate, final LocalDateTime lastDate) {
        //查询的条件
        Criteria operator = Criteria.where("coinMain").is(coinMain);
        if (StringUtils.isNotEmpty(coinOther)) {
            operator = Criteria.where("coinMain").is(coinMain).and("coinOther").is(coinOther);
        }
        if (startDate != null) {
            //有结束时间,添加结束时间条件
            operator.andOperator(Criteria.where("tradeTime").gte(startDate).lt(lastDate));
        } else {
            operator.andOperator(Criteria.where("tradeTime").lt(lastDate));
        }
        MatchOperation matchOperation = Aggregation.match(operator);
        if (StringUtils.isEmpty(coinOther)) {
            //只查询交易对
            GroupOperation groupOperation = Aggregation.group("coinOther", "coinMain");
            //获取的字段
            ProjectionOperation projectionOperation = Aggregation.project("coinOther", "coinMain");
            //构建查询
            return Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation);
        } else {
            //查询交易数据
            //获取的字段
            ProjectionOperation projectionOperation = Aggregation.project("coinOther", "coinMain",
                    "tradeTime", "minuteTime", "type", "price", "volume", "totalVolume");
            //构建查询
            return Aggregation.newAggregation(matchOperation, projectionOperation);
        }
    }

    @Override
    public List<RedisMatchStream> findIncrementTrades(String coinMain, String coinOther, Class<?> collectionName,
                                                      LocalDateTime startDate, LocalDateTime lastDate) {
        Aggregation aggregation = createIncrementAggregation(coinMain, coinOther, startDate, lastDate);
        AggregationResults<RedisMatchStream> tradePairVOsFlux = mongoTemplate.aggregate(aggregation, collectionName, RedisMatchStream.class);
        //返回结果
        return tradePairVOsFlux.getMappedResults();
    }

    private Aggregation createAggregation(final String coinMain, final LocalDateTime startDate, final LocalDateTime lastDate) {
        if (startDate == null) {
            return null;
        }
        //查询的条件
        Criteria operator = Criteria.where("coinMain").is(coinMain);
        if (lastDate != null) {
            //有结束时间,添加结束时间条件
            operator.andOperator(Criteria.where("tradeTime").gte(startDate).lte(lastDate));
        } else {
            operator.andOperator(Criteria.where("tradeTime").gte(startDate));
        }
        MatchOperation matchOperation = Aggregation.match(operator);
        //排序  用于获取最后一条记录  和 最开始的记录
        GroupOperation groupOperation = Aggregation.group("coinOther", "coinMain")
                .sum("volume").as("dayCount")
                .max("price").as("highestPrice")
                .min("price").as("lowerPrice")
                .last("price").as("latestPrice")
                .first("price").as("firstPrice");
        //获取的字段
        ProjectionOperation projectionOperation = Aggregation.project("latestPrice", "firstPrice", "highestPrice", "lowerPrice", "dayCount", "coinOther", "coinMain");
        //构建查询
        return Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation);
    }

    private Aggregation createAggregation(final String coinMain, final String sdate, final String edate) {
        //格式化当前时间
        LocalDateTime sLocalDateTime = LocalDateTime.now();
        if (sdate != null && StringUtils.isNotBlank(sdate)) {
            try {
                sLocalDateTime = DateUtils.parseLocalDateTime(sdate);
            } catch (ParseException e) {
                sLocalDateTime = LocalDateTime.now();
            }
        }
        sLocalDateTime = LocalDateTime.of(sLocalDateTime.toLocalDate(), LocalTime.of(0, 0, 0));
        //查询的条件
        LocalDateTime eLocalDateTime = null;
        if (edate != null) {
            try {
                eLocalDateTime = DateUtils.parseLocalDateTime(edate);
                eLocalDateTime = LocalDateTime.of(eLocalDateTime.toLocalDate(), LocalTime.of(23, 59, 59));
            } catch (ParseException ignored) {
            }
        }
        return createAggregation(coinMain, sLocalDateTime, eLocalDateTime);
    }
}
