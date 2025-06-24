package ru.t1.school.the_best_starter.kafka.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.t1.school.common.kafka.config.DefaultKafkaProducerConfig;
import ru.t1.school.common.kafka.dto.MetricsMessage;
import ru.t1.school.the_best_starter.config.PropertiesConfig;
import ru.t1.school.the_best_starter.config.StarterProperties;

@Configuration
@RequiredArgsConstructor
@Import(PropertiesConfig.class)
public class KafkaProducerConfig extends DefaultKafkaProducerConfig {

    private final StarterProperties starterProps;

    @Bean("metricsProducerFactory")
    public ProducerFactory<String, MetricsMessage> metricsProducerFactory() {
        return defaultProducerFactory(starterProps.getKafka());
    }

    @Bean("metricsKafkaTemplate")
    public KafkaTemplate<String, MetricsMessage> metricsKafkaTemplate() {
        KafkaTemplate<String, MetricsMessage> template = new KafkaTemplate<>(metricsProducerFactory());
        template.setDefaultTopic(starterProps.getKafka().getTopic().getMetrics());
        return template;
    }
}
