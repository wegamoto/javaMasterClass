package com.wewe.producer;

import org.springframework.stereotype.Service;

@Service
public class RssKafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public RssKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}