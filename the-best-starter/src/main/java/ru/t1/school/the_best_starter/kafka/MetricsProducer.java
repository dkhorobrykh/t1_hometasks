package ru.t1.school.the_best_starter.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import ru.t1.school.common.kafka.DefaultProducer;
import ru.t1.school.common.kafka.dto.MetricsMessage;
import ru.t1.school.common.kafka.dto.MetricsType;

@Slf4j
public class MetricsProducer extends DefaultProducer {
    private final KafkaTemplate<String, MetricsMessage> metricsKafkaTemplate;

    public MetricsProducer(KafkaTemplate<String, MetricsMessage> metricsKafkaTemplate) {
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
