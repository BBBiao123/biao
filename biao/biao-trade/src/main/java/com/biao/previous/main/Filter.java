package com.biao.previous.main;

import com.biao.kafka.interceptor.SampleMessage;
import com.biao.pojo.TradeDto;
import com.biao.previous.domain.ProcessData;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

/**
 *
 * @date 2018/4/7
 * 数据处理。过滤
 */
public interface Filter {
    /**
     * 处理数据。
     * 如果传入的数据为null有示结束这个filter处理
     *
     * @param data  数据对象；
     * @param chain 链
     */
    void doFilter(TradeDto data, ProcessData processData, DataChain chain);

    /**
     * 设置一下Redis;
     *
     * @param template temp
     */
    Filter setRedis(RedisTemplate template);

    /**
     * 设置RedissionClient的客户端；
     *
     * @param redisson redissio;
     * @return 近回
     */
    Filter setRedisson(RedissonClient redisson);

    /**
     * Kafka处理；
     *
     * @param kafka 对象；
     * @return 返回；
     */
    Filter setKafka(KafkaTemplate<Object, SampleMessage> kafka);
}
