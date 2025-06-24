package ru.t1.school.the_best_starter.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import ru.t1.school.common.kafka.dto.MetricsMessage;
import ru.t1.school.the_best_starter.kafka.MetricsProducer;
import ru.t1.school.the_best_starter.kafka.config.KafkaProducerConfig;

@Configuration
@Import({KafkaProducerConfig.class})
public class KafkaConfig {
    @Bean
    public MetricsProducer metricsProducer(@Qualifier("metricsKafkaTemplate") KafkaTemplate<String, MetricsMessage> metricsKafkaTemplate) {
        return new MetricsProducer(metricsKafkaTemplate);
    }
}
