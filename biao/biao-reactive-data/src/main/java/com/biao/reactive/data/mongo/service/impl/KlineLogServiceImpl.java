package com.biao.reactive.data.mongo.service.impl;

import com.biao.reactive.data.mongo.domain.kline.KlineLog;
import com.biao.reactive.data.mongo.service.KlineLogService;
import com.biao.util.DateUtils;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("klineLogService")
public class KlineLogServiceImpl implements KlineLogService {

    private static Logger logger = LoggerFactory.getLogger(KlineLogServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void batchInsert(List<KlineLog> klineLog) {
        mongoTemplate.insertAll(klineLog);
    }

    @Override
    public void insert(KlineLog klineLog) {
        mongoTemplate.insert(klineLog);
    }

    @Override
    public void delete(String coinMain, String coinOther, LocalDateTime startDate, LocalDateTime lastDate) {
        Query query = new Query();
        StringBuffer logBuffer = new StringBuffer();
        query.addCriteria(Criteria.where("coinMain").is(coinMain));
        logBuffer.append("coinMain=" + coinMain);
        query.addCriteria(Criteria.where("coinOther").is(coinOther));
        logBuffer.append(",coinOther=" + coinOther);
        if (startDate != null && lastDate == null) {
            query.addCriteria(Criteria.where("tradeTime").gte(startDate));
            logBuffer.append(",startTime=" + DateUtils.formaterLocalDateTime(startDate));
        }
        if (startDate == null && lastDate != null) {
            query.addCriteria(Criteria.where("tradeTime").lt(lastDate));
            logBuffer.append(",lastDate=" + DateUtils.formaterLocalDateTime(lastDate));
        }
        if (startDate != null && lastDate != null) {
            //有结束时间,添加结束时间条件
            query.addCriteria(Criteria.where("tradeTime").gte(startDate).lt(lastDate));
            logBuffer.append(",startTime=" + DateUtils.formaterLocalDateTime(startDate) + ",lastDate=" + DateUtils.formaterLocalDateTime(lastDate));
        }
        DeleteResult deleteResult = mongoTemplate.remove(query, KlineLog.class);
        logger.info("删除mongodb的交易数据.删除大小size:{},删除条件condition:{}", deleteResult.getDeletedCount(), logBuffer.toString());
    }

    @Override
    public List<KlineLog> findStatisticsTradeByCoinMain(String coinMain, String coinOther, Class<?> collectionName,
                                                        LocalDateTime startDate, LocalDateTime lastDate) {
        Aggregation aggregation = createIncrementAggregation(coinMain, coinOther, startDate, lastDate);
        AggregationResults<KlineLog> tradePairVOsFlux = mongoTemplate.aggregate(aggregation, collectionName, KlineLog.class);
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
                    "tradeTime", "minuteTime", "price", "volume");
            //构建查询
            return Aggregation.newAggregation(matchOperation, projectionOperation);
        }
    }
}
