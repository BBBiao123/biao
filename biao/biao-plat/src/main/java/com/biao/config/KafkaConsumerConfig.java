package com.biao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    /**
     * 批量接受数据</br>
     * ex: @KafkaListener(id = "list", topics = "myTopic", containerFactory = "batchFactory")
     * public void listen(List<String> list) {
     * ...
     * }
     * </br>
     * ex2: add factory.setMessageConverter(new BatchMessagingMessageConverter(converter()))
     *
     * @KafkaListener(topics = "blc1")
     * public void listen(List<Foo> foos, @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
     * ...
     * }
     */
    @Bean
    public KafkaListenerContainerFactory<?> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * 默认值
     * fetch.min.bytes = 1
     * fetch.max.bytes = 50 * 1024 * 1024
     * max.partition.fetch.bytes = 1 * 1024 * 1024
     * max.poll.records = 500
     * 单位ms
     * fetch.max.wait.ms = 500
     * max.poll.interval.ms = 300000
     * heartbeat.interval.ms = 3000
     *
     * @return
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties();
        return properties;
    }
}
