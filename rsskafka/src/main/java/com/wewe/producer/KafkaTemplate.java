package com.wewe.producer;

import com.wewe.config.ProducerFactory;

public class KafkaTemplate<T, T1> {
    public KafkaTemplate(ProducerFactory<T1, T1> stringStringProducerFactory) {
    }

    public void send(T1 topic, T1 message) {
    }
}
