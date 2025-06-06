package ru.t1.school.main_project.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.t1.school.common.kafka.DefaultProducer;
import ru.t1.school.common.kafka.dto.MetricsMessage;
import ru.t1.school.common.kafka.dto.MetricsType;

@Slf4j
@Component
public class MetricsProducer extends DefaultProducer {
    private final KafkaTemplate<String, MetricsMessage> metricsKafkaTemplate;

    public MetricsProducer(@Qualifier("metricsKafkaTemplate") KafkaTemplate<String, MetricsMessage> metricsKafkaTemplate) {
        this.metricsKafkaTemplate = metricsKafkaTemplate;
    }

    public boolean send(MetricsMessage message, MetricsType type) {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.KEY, type.name())
                .setHeader(KafkaHeaders.TOPIC, metricsKafkaTemplate.getDefaultTopic())
                .setHeader("type", type.name())
                .build();

        return send(metricsKafkaTemplate, msg);
    }
}
