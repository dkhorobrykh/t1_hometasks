package ru.t1.school.main_project.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.t1.school.main_project.kafka.dto.MetricsMessage;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${kafka.bootstrap.server}")
    private String servers;
    @Value("${kafka.topic.metrics:t1_demo_metrics}")
    private String metricsTopic;
    @Value("${kafka.producer.retries:3}")
    private int producerRetries;
    @Value("${kafka.producer.retry-backoff-ms:1000}")
    private int producerRetryBackoffMs;
    @Value("${kafka.producer.enable-idempotence:true}")
    private boolean producerEnableIdempotence;
    @Value("${kafka.producer.max-block-ms:10000}")
    private int producerMaxBlockMs;
    @Value("${kafka.producer.request-timeout-ms:5000}")
    private int producerRequestTimeoutMs;
    @Value("${kafka.producer.delivery-timeout-ms:10000}")
    private int producerDeliveryTimeoutMs;

    @Bean
    public ProducerFactory<String, MetricsMessage> metricsProducerFactory() {
        return defaultProducerFactory();
    }

    @Bean
    public KafkaTemplate<String, MetricsMessage> metricsKafkaTemplate() {
        KafkaTemplate<String, MetricsMessage> template = new KafkaTemplate<>(metricsProducerFactory());
        template.setDefaultTopic(metricsTopic);
        return template;
    }

    private <T> ProducerFactory<String, T> defaultProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.RETRIES_CONFIG, producerRetries);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, producerRetryBackoffMs);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, producerEnableIdempotence);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, producerMaxBlockMs);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerRequestTimeoutMs);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, producerDeliveryTimeoutMs);
        return new DefaultKafkaProducerFactory<>(props);
    }

}
