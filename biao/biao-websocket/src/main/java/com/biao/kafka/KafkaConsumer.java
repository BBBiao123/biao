/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.biao.kafka;

import com.biao.cache.PlatOrderCacheManager;
import com.biao.config.TradeKafkaConsumer;
import com.biao.constant.RedisKeyConstant;
import com.biao.constant.TradeConstant;
import com.biao.entity.OfflineOrderDetail;
import com.biao.handler.PlatPushDataHandler;
import com.biao.kafka.interceptor.MessageDTO;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.pojo.KafkaPlatOrderDTO;
import com.biao.pojo.MatchStreamDto;
import com.biao.pojo.TradeDto;
import com.biao.pojo.UserOrderDTO;
import com.biao.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * TradePair kafka Consumer.
 *
 *  ""
 */
@Component
@SuppressWarnings("all")
public class KafkaConsumer {

    private final PlatPushDataHandler platPushDataHandler;

    private final KafkaProperties kafkaProperties;

    private final PlatOrderCacheManager platOrderCacheManager;

    @Autowired
    public KafkaConsumer(final PlatPushDataHandler platPushDataHandler,
                         final KafkaProperties kafkaProperties,
                         final PlatOrderCacheManager platOrderCacheManager) {
        this.platPushDataHandler = platPushDataHandler;
        this.kafkaProperties = kafkaProperties;
        this.platOrderCacheManager = platOrderCacheManager;
    }


    /**
     * 推送C2C订单给用户.
     *
     * @param sampleMessages message
     */
    /**
     * pushUserPartSuccessVolume.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(topics = TradeConstant.C2C_USER_ORDER)
    public void pushC2cUserOrder(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> {
            final OfflineOrderDetail message = sampleMessage.getMessage(OfflineOrderDetail.class);
            if (Objects.nonNull(message)) {
                platPushDataHandler.pushC2CData(message.getUserId(), JsonUtils.toJson(message));
            }

        });
    }

    /**
     * 推送订单给用户.
     *
     * @param sampleMessages message
     */
    @KafkaListener(topics = TradeConstant.TRADE_KAFKA_ORDER_BUYANDSELL_TOPIC)
    public void sendOrder(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> {
            TradeDto dto = sampleMessage.getMessage(TradeDto.class);
            platPushDataHandler.pushTradeOrder(dto);
        });

    }


    /**
     * 推送message给用户.
     *
     * @param sampleMessages message
     */
    @KafkaListener(topics = TradeConstant.MESSAGE)
    public void sendMessage(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> {
            MessageDTO dto = sampleMessage.getMessage(MessageDTO.class);
            platPushDataHandler.pushMessage(dto);
        });

    }


    /**
     * 缓存平台挂单。
     *
     * @param sampleMessages message
     */
    @KafkaListener(topics = TradeConstant.TRADE_KAFKA_ORDER_TOPIC)
    public void cachePlatOrder(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> {
            KafkaPlatOrderDTO kpd = sampleMessage.getMessage(KafkaPlatOrderDTO.class);
            //加入数据;
            if (Objects.equals(kpd.getAction(), KafkaPlatOrderDTO.Action.PUT)) {
                platOrderCacheManager.addCache(kpd.getTradeDto());
            } else if (Objects.equals(kpd.getAction(), KafkaPlatOrderDTO.Action.REMOVE)) { //删除数据;
                platOrderCacheManager.removeCache(kpd.getRemoveTradeDto());
            }
        });

    }

    /**
     * 推送统计的交易对数据.
     *
     * @param sampleMessages message
     */
    @KafkaListener(topics = RedisKeyConstant.TRADE_STATISTIC_TOPIC)
    public void processTradeExpair(final List<SampleMessage> sampleMessages) {
        //sampleMessages.forEach(m -> platPushDataHandler.pushTradeExPairData(m.getMessage().toString()));

    }

    /**
     * pushUserPartSuccessVolume.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(topics = TradeConstant.TRADE_RESULT_ORDER_TOPIC)
    public void pushUserPartSuccessVolume(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> platPushDataHandler.pushUserPartSuccessVolume(sampleMessage.getMessage(UserOrderDTO.class)));
    }

    /**
     * 推送撮合结果到前端.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(id = "#{tradeKafkaConsumer0.groupId}", topicPartitions = {
            @TopicPartition(topic = "#{tradeKafkaConsumer0.topic}", partitions = "#{tradeKafkaConsumer0.partitions}")
    }, beanRef = "tradeKafkaConsumer0")
    public void processMatchResult0(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> platPushDataHandler.pushMatchStream(sampleMessage.getMessage(MatchStreamDto.class)));
    }

    /**
     * 推送撮合结果到前端.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(id = "#{tradeKafkaConsumer1.groupId}", topicPartitions = {
            @TopicPartition(topic = "#{tradeKafkaConsumer1.topic}", partitions = "#{tradeKafkaConsumer1.partitions}")
    }, beanRef = "tradeKafkaConsumer1")
    public void processMatchResult1(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> platPushDataHandler.pushMatchStream(sampleMessage.getMessage(MatchStreamDto.class)));

    }

    /**
     * 推送撮合结果到前端.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(id = "#{tradeKafkaConsumer2.groupId}", topicPartitions = {
            @TopicPartition(topic = "#{tradeKafkaConsumer2.topic}", partitions = "#{tradeKafkaConsumer2.partitions}")
    }, beanRef = "tradeKafkaConsumer2")
    public void processMatchResult2(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> platPushDataHandler.pushMatchStream(sampleMessage.getMessage(MatchStreamDto.class)));

    }

    /**
     * 推送撮合结果到前端.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(id = "#{tradeKafkaConsumer3.groupId}", topicPartitions = {
            @TopicPartition(topic = "#{tradeKafkaConsumer3.topic}", partitions = "#{tradeKafkaConsumer3.partitions}")
    }, beanRef = "tradeKafkaConsumer3")
    public void processMatchResult3(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> platPushDataHandler.pushMatchStream(sampleMessage.getMessage(MatchStreamDto.class)));

    }

    /**
     * 推送撮合结果到前端.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(id = "#{tradeKafkaConsumer4.groupId}", topicPartitions = {
            @TopicPartition(topic = "#{tradeKafkaConsumer4.topic}", partitions = "#{tradeKafkaConsumer4.partitions}")
    }, beanRef = "tradeKafkaConsumer4")
    public void processMatchResult4(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> platPushDataHandler.pushMatchStream(sampleMessage.getMessage(MatchStreamDto.class)));
    }

    /**
     * 推送撮合结果到前端.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(id = "#{tradeKafkaConsumer5.groupId}", topicPartitions = {
            @TopicPartition(topic = "#{tradeKafkaConsumer5.topic}", partitions = "#{tradeKafkaConsumer5.partitions}")
    }, beanRef = "tradeKafkaConsumer5")
    public void processMatchResult5(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> platPushDataHandler.pushMatchStream(sampleMessage.getMessage(MatchStreamDto.class)));

    }

    /**
     * 推送撮合结果到前端.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(id = "#{tradeKafkaConsumer6.groupId}", topicPartitions = {
            @TopicPartition(topic = "#{tradeKafkaConsumer6.topic}", partitions = "#{tradeKafkaConsumer6.partitions}")
    }, beanRef = "tradeKafkaConsumer6")
    public void processMatchResult6(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> platPushDataHandler.pushMatchStream(sampleMessage.getMessage(MatchStreamDto.class)));

    }

    /**
     * 推送撮合结果到前端.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(id = "#{tradeKafkaConsumer7.groupId}", topicPartitions = {
            @TopicPartition(topic = "#{tradeKafkaConsumer7.topic}", partitions = "#{tradeKafkaConsumer7.partitions}")
    }, beanRef = "tradeKafkaConsumer7")
    public void processMatchResult7(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage -> platPushDataHandler.pushMatchStream(sampleMessage.getMessage(MatchStreamDto.class)));

    }

    @Bean
    public TradeKafkaConsumer tradeKafkaConsumer0() {
        String partitionExpairPress = consumerConfigsIndex(0);
        return new TradeKafkaConsumer("stream0", TradeConstant.TRADE_RESULT_MATCH_TOPIC, getParttions(partitionExpairPress));
    }

    @Bean
    public TradeKafkaConsumer tradeKafkaConsumer1() {
        String partitionExpairPress = consumerConfigsIndex(1);
        return new TradeKafkaConsumer("stream1", TradeConstant.TRADE_RESULT_MATCH_TOPIC, getParttions(partitionExpairPress));
    }

    @Bean
    public TradeKafkaConsumer tradeKafkaConsumer2() {
        String partitionExpairPress = consumerConfigsIndex(2);
        return new TradeKafkaConsumer("stream2", TradeConstant.TRADE_RESULT_MATCH_TOPIC, getParttions(partitionExpairPress));
    }

    @Bean
    public TradeKafkaConsumer tradeKafkaConsumer3() {
        String partitionExpairPress = consumerConfigsIndex(3);
        return new TradeKafkaConsumer("stream3", TradeConstant.TRADE_RESULT_MATCH_TOPIC, getParttions(partitionExpairPress));
    }

    @Bean
    public TradeKafkaConsumer tradeKafkaConsumer4() {
        String partitionExpairPress = consumerConfigsIndex(4);
        return new TradeKafkaConsumer("stream4", TradeConstant.TRADE_RESULT_MATCH_TOPIC, getParttions(partitionExpairPress));
    }

    @Bean
    public TradeKafkaConsumer tradeKafkaConsumer5() {
        String partitionExpairPress = consumerConfigsIndex(5);
        return new TradeKafkaConsumer("stream5", TradeConstant.TRADE_RESULT_MATCH_TOPIC, getParttions(partitionExpairPress));
    }

    @Bean
    public TradeKafkaConsumer tradeKafkaConsumer6() {
        String partitionExpairPress = consumerConfigsIndex(6);
        return new TradeKafkaConsumer("stream6", TradeConstant.TRADE_RESULT_MATCH_TOPIC, getParttions(partitionExpairPress));
    }

    @Bean
    public TradeKafkaConsumer tradeKafkaConsumer7() {
        String partitionExpairPress = consumerConfigsIndex(7);
        return new TradeKafkaConsumer("stream7", TradeConstant.TRADE_RESULT_MATCH_TOPIC, getParttions(partitionExpairPress));
    }


    private String[] consumerConfigs() {
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties();
        String partitionerExpairStr = (String) properties.get("partitioner.expair");
        return partitionerExpairStr.split(",");
    }

    private String consumerConfigsIndex(int index) {
        String[] partitionerExpair = consumerConfigs();
        return partitionerExpair[index];
    }

    private String[] parsePartitions(int start, int end) {
        StringBuilder partitionBuilder = new StringBuilder();
        for (int i = start; i <= end; i++) {
            partitionBuilder.append(i + ",");
        }
        String partitions = partitionBuilder.toString();
        partitions = partitions.substring(0, partitions.length() - 1);
        return partitions.split(",");
    }

    private String[] getParttions(String partitionExpairPress) {
        String[] partitionExpairs = partitionExpairPress.split("-");
        return parsePartitions(Integer.parseInt(partitionExpairs[0]), Integer.parseInt(partitionExpairs[1]));
    }

}
