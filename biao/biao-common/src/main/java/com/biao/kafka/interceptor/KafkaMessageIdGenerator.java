package com.biao.kafka.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
import java.util.UUID;

public class KafkaMessageIdGenerator implements ProducerInterceptor<String, SampleMessage> {

    @Override
    public void configure(Map<String, ?> configs) {

    }

    @Override
    public ProducerRecord<String, SampleMessage> onSend(ProducerRecord<String, SampleMessage> record) {
        if (!(record.value() instanceof SampleMessage)) {
            return record;
        }
        SampleMessage message = record.value();
        if (message != null && StringUtils.isBlank(message.getId())) {
            message.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        return new ProducerRecord<String, SampleMessage>(record.topic(), record.partition(), System.currentTimeMillis(), record.key(), message, record.headers());
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

}
