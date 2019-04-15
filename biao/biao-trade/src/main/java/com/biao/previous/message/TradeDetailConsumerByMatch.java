package com.biao.previous.message;

import com.biao.constant.TradeConstant;
import com.biao.enums.OrderEnum;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.pojo.MatchStreamDto;
import com.biao.pojo.UserOrderDTO;
import com.biao.reactive.data.mongo.domain.MatchStream;
import com.biao.reactive.data.mongo.domain.TradeDetail;
import com.biao.util.DateUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 把详情写入的到mongo中；
 *
 *
 */
public class TradeDetailConsumerByMatch implements TradeExecutorSubscriber<TradeDetail> {
    /**
     * 数据库操作类；
     */
    private ReactiveMongoTemplate mongoTemplate;
    /**
     * 日志打印;
     */
    private Logger logger = LoggerFactory.getLogger(TradeDetailConsumerByMatch.class);
    /**
     * 发送一个kafka消息；
     */
    private KafkaTemplate kafkaTemplate;

    public TradeDetailConsumerByMatch(ReactiveMongoTemplate mongoTemplate, KafkaTemplate kafkaTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Flux<String> executor(Collection<? extends TradeDetail> collections) {
        List<MatchStream> details = collections.stream()
                .filter(e -> Objects.equals(e.getMainTrade().ordinal(), e.getType()))
                .map(e -> {
                    MatchStream m = new MatchStream();
                    m.setOrderNo(e.getOrderNo());
                    m.setCoinMain(e.getCoinMain());
                    m.setCoinOther(e.getCoinOther());
                    m.setMinuteTime(e.getMinuteTime());
                    m.setTradeTime(e.getTradeTime());
                    m.setType(e.getMainTrade().ordinal());
                    m.setVolume(e.getTradeVolume());
                    m.setTradeNo(e.getTradeNo());
                    m.setUserId(e.getUserId());
                    m.setToUserId(e.getToUserId());
                    m.setStatus(e.getStatus());
                    m.setPrice(e.getLastPrice());
                    return m;
                }).collect(Collectors.toList());
        //查到一条买数据的最大状态值 ；
        List<UserOrderDTO> usds = Lists.newArrayList();
        collections.stream().collect(Collectors.groupingBy(TradeDetail::getType, Collectors.toList()))
                .forEach((k, v) -> v.stream()
                        .filter(e -> Objects.equals(e.getStatus(), OrderEnum.OrderStatus.ALL_SUCCESS))
                        .collect(Collectors.toList())
                        .forEach(e -> {
                            UserOrderDTO usd = new UserOrderDTO();
                            usd.setExType(k);
                            usd.setOrderNo(e.getOrderNo());
                            usd.setStatus(e.getStatus().getCode());
                            usd.setUserId(e.getUserId());
                            usd.setCoinMain(e.getCoinMain());
                            usd.setCoinMain(e.getCoinOther());
                            usd.setSuccessVolume(BigDecimal.ZERO);
                            usds.add(usd);
                        }));
        //订单数据推送。
        Flux.fromIterable(usds).subscribe(e -> {
            String key = e.getCoinMain() + "_" + e.getCoinOther();
            kafkaTemplate.send(TradeConstant.TRADE_RESULT_ORDER_TOPIC, key, SampleMessage.build(e));
        });
        //发送推送结果；
        Flux.fromIterable(details).map(e -> {
            MatchStreamDto dto = new MatchStreamDto();
            dto.setCoinMain(e.getCoinMain());
            dto.setCoinOther(e.getCoinOther());
            dto.setMinuteTime(DateUtils.formaterLocalDateTime(e.getMinuteTime()));
            dto.setPrice(e.getPrice());
            dto.setOrderNo(e.getOrderNo());
            dto.setTradeTime(DateUtils.formaterLocalDateTime(e.getTradeTime()));
            dto.setType(e.getType());
            dto.setVolume(e.getVolume());
            dto.setTradeNo(e.getTradeNo());
            dto.setUserId(e.getUserId());
            dto.setToUserId(e.getToUserId());
            return dto;
        }).subscribe(e ->
        {
            String key = e.getCoinMain() + "_" + e.getCoinOther();
            kafkaTemplate.send(TradeConstant.TRADE_RESULT_MATCH_TOPIC, key, SampleMessage.build(e));
        });
        return mongoTemplate.insertAll(details)
                .doOnError(e -> logger.error("更新mongodb失败了{}", e))
                .flatMap(e -> Flux.just("MATCH_插入流水(Match)数据到mongodb成功!订单号：" + e.getOrderNo() + "交易号：" + e.getTradeNo()));
    }

}
