package com.biao.reactive.data.mongo.service.impl;

import com.biao.pojo.ResponsePage;
import com.biao.query.UserTradeQuery;
import com.biao.reactive.data.mongo.domain.TradeDetail;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.util.DateUtil;
import com.biao.vo.ReportTradeFreeVO;
import com.biao.vo.TradeExPairDayAvgVO;
import com.biao.vo.UserTradeVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * tradeDetailService.
 *
 *  ""
 */
@Service("tradeDetailService")
public class TradeDetailServiceImpl implements TradeDetailService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据查询条件获取用户交易对详情.
     *
     * @param userTradeQuery 查询条件
     * @return List UserTradeVO
     */
    @Override
    public ResponsePage<UserTradeVO> findByPage(final UserTradeQuery userTradeQuery) {

        ResponsePage<UserTradeVO> responsePage = new ResponsePage<>();

        Query query = new Query();

        if (StringUtils.isNoneBlank(userTradeQuery.getUserId())) {
            query.addCriteria(new Criteria("userId").is(userTradeQuery.getUserId()));
        }
        if (StringUtils.isNoneBlank(userTradeQuery.getCoinMain())) {
            query.addCriteria(new Criteria("coinMain").is(userTradeQuery.getCoinMain()));
        }
        if (StringUtils.isNoneBlank(userTradeQuery.getCoinOther())) {
            query.addCriteria(new Criteria("coinOther").is(userTradeQuery.getCoinOther()));
        }

        final Long totalCount = mongoTemplate.count(query, TradeDetail.class);
        if (Objects.isNull(totalCount) || totalCount <= 0) {
            return responsePage;
        }
        responsePage.setCount(totalCount);

        Sort sort = new Sort(Sort.Direction.DESC, "tradeTime");

        Pageable pageable = PageRequest.of(userTradeQuery.getCurrentPage() - 1, userTradeQuery.getShowCount(), sort);

        query.with(pageable);
        final List<TradeDetail> tradeDetailFlux = mongoTemplate.find(query, TradeDetail.class);

        List<UserTradeVO> collect = Objects.requireNonNull(tradeDetailFlux)
                .stream()
                .map(this::buildUserTradeVO).collect(Collectors.toList());
        responsePage.setList(collect);
        return responsePage;
    }

    /**
     * 根据订单号/id 获取交易详情.
     *
     * @param orderNo 订单id
     * @return List UserTradeVO
     */
    @Override
    public List<UserTradeVO> findByOrderNo(final String orderNo) {

        Query query = new Query();

        if (StringUtils.isNoneBlank(orderNo)) {
            query.addCriteria(new Criteria("orderNo").is(orderNo));
        }

        final List<TradeDetail> tradeDetailFlux = mongoTemplate.find(query, TradeDetail.class);

        return Objects.requireNonNull(tradeDetailFlux)
                .stream().map(this::buildUserTradeVO)
                .collect(Collectors.toList());

    }

    /**
     * 根据交易号，订单号获取唯一条详情.
     *
     * @param tradeNo 交易号
     * @param orderNo 挂单号
     * @return UserTradeVO
     */
    @Override
    public UserTradeVO findByTradeNoAndOrderNo(final String tradeNo, final String orderNo) {
        Query query = new Query();

        if (StringUtils.isNoneBlank(orderNo)) {
            query.addCriteria(new Criteria("orderNo").is(orderNo));
        }

        if (StringUtils.isNoneBlank(tradeNo)) {
            query.addCriteria(new Criteria("tradeNo").is(tradeNo));
        }

        final List<TradeDetail> tradeDetailFlux = mongoTemplate.find(query, TradeDetail.class);

        return Objects.requireNonNull(tradeDetailFlux)
                .stream().map(this::buildUserTradeVO)
                .collect(Collectors.toList()).get(0);
    }

    /**
     * 根据交易号，type获取唯一条详情.
     *
     * @param tradeNo 交易号
     * @param type    type
     * @return UserTradeVO
     */
    @Override
    public UserTradeVO findByTradeNoAndType(final String tradeNo, final Integer type) {
        Query query = new Query();

        if (Objects.nonNull(type)) {
            query.addCriteria(new Criteria("type").is(type));
        }

        if (StringUtils.isNoneBlank(tradeNo)) {
            query.addCriteria(new Criteria("tradeNo").is(tradeNo));
        }

        final List<TradeDetail> tradeDetailFlux = mongoTemplate.find(query, TradeDetail.class);

        return Objects.requireNonNull(tradeDetailFlux)
                .stream().map(this::buildUserTradeVO)
                .collect(Collectors.toList()).get(0);
    }

    @Override
    public List<ReportTradeFreeVO> countTradeFree(Integer type, final LocalDateTime startDate, final LocalDateTime lastDate) {
        Aggregation aggregation = createAggregation(type, startDate, lastDate);
        AggregationResults<ReportTradeFreeVO> tradePairVOsFlux = mongoTemplate.aggregate(aggregation, TradeDetail.class, ReportTradeFreeVO.class);
        //返回结果
        return tradePairVOsFlux.getMappedResults();
    }

    private Aggregation createAggregation(Integer type, final LocalDateTime startDate, final LocalDateTime lastDate) {
        if (startDate == null) {
            return null;
        }
        //查询的条件
        Criteria operator = Criteria.where("type").is(type);
        if (lastDate != null) {
            //有结束时间,添加结束时间条件
            operator.andOperator(Criteria.where("tradeTime").gte(startDate).lte(lastDate));
        } else {
            operator.andOperator(Criteria.where("tradeTime").gte(startDate));
        }
        MatchOperation matchOperation = Aggregation.match(operator);
        //排序  用于获取最后一条记录  和 最开始的记录
        GroupOperation groupOperation = Aggregation.group("coinOther", "coinMain")
                .sum("exFee").as("sumFee");
        //获取的字段
        ProjectionOperation projectionOperation = Aggregation.project("sumFee", "coinOther", "coinMain");
        //构建查询
        return Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation);
    }

    private UserTradeVO buildUserTradeVO(final TradeDetail tradeDetail) {
        UserTradeVO userTradeVO = new UserTradeVO();
        userTradeVO.setId(tradeDetail.getId());
        userTradeVO.setCreateDate(tradeDetail.getTradeTime());
        userTradeVO.setCoinOther(tradeDetail.getCoinOther());
        userTradeVO.setCoinMain(tradeDetail.getCoinMain());
        userTradeVO.setExFee(tradeDetail.getExFee());
        final BigDecimal lastPrice = tradeDetail.getLastPrice();
        if (Objects.isNull(lastPrice)) {
            userTradeVO.setPrice(tradeDetail.getPrice());
        } else {
            userTradeVO.setPrice(lastPrice);
        }

        userTradeVO.setSuccessVolume(tradeDetail.getTradeVolume());
        userTradeVO.setExType(tradeDetail.getType());
        userTradeVO.setUserId(tradeDetail.getUserId());
        userTradeVO.setToUserId(tradeDetail.getToUserId());
        userTradeVO.setOrderNo(tradeDetail.getOrderNo());
        userTradeVO.setTradeNo(tradeDetail.getTradeNo());
        return userTradeVO;
    }

    @Override
    public TradeExPairDayAvgVO findDayAverageByExPair(String date, String coinOne, String coinOther) {

        if (StringUtils.isBlank(date) || StringUtils.isBlank(coinOne) || StringUtils.isBlank(coinOther)) {
            return null;
        }

        //查询的条件
        Criteria operator = Criteria.where("coinMain").is(coinOne).and("coinOther").is(coinOther);
        Date startTime = DateUtil.parseDateTime(date.concat(" 00:00:00"));
        Date endTime = DateUtil.parseDateTime(date.concat(" 23:59:59"));
        operator.andOperator(Criteria.where("tradeTime").gte(startTime).lte(endTime));


        MatchOperation matchOperation = Aggregation.match(operator);
        //求平均值,avg(price)
        GroupOperation groupOperation = Aggregation.group("coinOther", "coinMain").avg("price").as("avgPrice");
        //返回的字段
        ProjectionOperation projectionOperation = Aggregation.project("avgPrice", "coinOther", "coinMain");
        //构建查询
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation);
        AggregationResults<TradeExPairDayAvgVO> tradePairVOs = mongoTemplate.aggregate(aggregation, TradeDetail.class, TradeExPairDayAvgVO.class);

        //返回结果
        return CollectionUtils.isEmpty(tradePairVOs.getMappedResults()) ? null : tradePairVOs.getMappedResults().get(0);
    }

    @Override
    public List<UserTradeVO> findByDate(String startDate, String endDate) {
        return this.findByDateAndCoinMain(startDate, endDate, null);
    }

    @Override
    public List<UserTradeVO> findByDateAndCoinMain(String startDate, String endDate, String coinMain) {

        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return null;
        }

        Query query = new Query();
        Date startTime = DateUtil.parseDateTime(startDate);
        Date endTime = DateUtil.parseDateTime(endDate);
        query.addCriteria(new Criteria("tradeTime").gt(startTime).lte(endTime));

        if (StringUtils.isNotBlank(coinMain)) {
            query.addCriteria(new Criteria("coinMain").is(coinMain));
        }

        final List<TradeDetail> tradeDetailFlux = mongoTemplate.find(query, TradeDetail.class);
        return Objects.requireNonNull(tradeDetailFlux)
                .stream().map(this::buildUserTradeVO)
                .collect(Collectors.toList());
    }

}
