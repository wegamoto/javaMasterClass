package com.wewe.consumer;

import org.springframework.stereotype.Service;

@Service
public class RssKafkaConsumer {

    @KafkaListener(topics = "rss-topic", groupId = "rss-consumer-group")
    public void consume(String message) {
        System.out.println("Received: " + message);
    }
}
