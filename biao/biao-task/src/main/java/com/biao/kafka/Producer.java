package com.biao.kafka;

import com.biao.kafka.interceptor.SampleMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class Producer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    Producer(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Object message) {
        SampleMessage sampleMessage = new SampleMessage();
        sampleMessage.setMessage(message);
        this.kafkaTemplate.send("testGroup", message);
        System.out.println("Sent sample message [" + message + "]");
    }

    public void send(String topic, Object message) {
        SampleMessage sampleMessage = new SampleMessage();
        sampleMessage.setMessage(message);
        this.kafkaTemplate.send(topic, sampleMessage);
        System.out.println("Sent sample message [" + message + "]");
    }

    public void send(String topic, String key, Object message) {
        SampleMessage sampleMessage = new SampleMessage();
        sampleMessage.setMessage(message);
        this.kafkaTemplate.send(topic, key, sampleMessage);
        System.out.println("Sent sample message [" + message + "]");
    }

    public void send(String topic, String key, int parttion, Object message) {
        SampleMessage sampleMessage = new SampleMessage();
        sampleMessage.setMessage(message);
        this.kafkaTemplate.send(topic, parttion, key, sampleMessage);
        System.out.println("Sent sample message [" + message + "]");
    }

    public void send(String topic, String key, int parttion, Object message, ListenableFutureCallback<SendResult<Object, Object>> callback) {
        SampleMessage sampleMessage = new SampleMessage();
        sampleMessage.setMessage(message);
        ListenableFuture<SendResult<Object, Object>> listenableFuture = this.kafkaTemplate.send(topic, parttion, key, sampleMessage);
        listenableFuture.addCallback(callback);
        System.out.println("Sent sample message [" + message + "]");
    }
}
