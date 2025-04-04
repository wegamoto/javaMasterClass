package com.wewe.producer;

import com.wewe.config.ProducerFactory;

import java.util.Map;

public class DefaultKafkaProducerFactory extends ProducerFactory<String, String> {
    public DefaultKafkaProducerFactory(Map<String, Object> config) {
        super();
    }
}
