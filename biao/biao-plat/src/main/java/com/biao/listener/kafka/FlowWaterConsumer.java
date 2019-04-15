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

package com.biao.listener.kafka;

import com.biao.cache.FlowingWaterCacheManager;
import com.biao.constant.TradeConstant;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.pojo.MatchStreamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Flow water consumer.
 *
 *  ""
 */
@Component
public class FlowWaterConsumer {

    private final FlowingWaterCacheManager flowingWaterCacheManager;

    /**
     * Instantiates a new Flow water consumer.
     *
     * @param flowingWaterCacheManager the flowing water cache manager
     */
    @Autowired
    public FlowWaterConsumer(final FlowingWaterCacheManager flowingWaterCacheManager) {
        this.flowingWaterCacheManager = flowingWaterCacheManager;
    }


    /**
     * 监听流水，缓存.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(topics = TradeConstant.TRADE_RESULT_MATCH_TOPIC)
    public void processMatchResult0(final List<SampleMessage> sampleMessages) {
        sampleMessages.forEach(sampleMessage ->
                flowingWaterCacheManager.refresh(sampleMessage.getMessage(MatchStreamDto.class)));
    }


}
