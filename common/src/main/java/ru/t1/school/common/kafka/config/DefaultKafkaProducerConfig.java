package ru.t1.school.common.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public abstract class DefaultKafkaProducerConfig {
    protected <T> ProducerFactory<String, T> defaultProducerFactory(DefaultKafkaProperties kafkaProps) {
        var producerProps = kafkaProps.getProducer();
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.RETRIES_CONFIG, producerProps.getRetries());
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, producerProps.getRetryBackoffMs());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, producerProps.getEnableIdempotence());
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, producerProps.getMaxBlockMs());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerProps.getRequestTimeoutMs());
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, producerProps.getDeliveryTimeoutMs());
        return new DefaultKafkaProducerFactory<>(props);
    }
}
