package ru.t1.school.main_project.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.t1.school.main_project.kafka.dto.MetricsMessage;
import ru.t1.school.main_project.kafka.dto.MetricsType;

@Slf4j
@Component
public class MetricsProducer<T extends MetricsMessage> {
    private final KafkaTemplate<String, MetricsMessage> metricsKafkaTemplate;

    public MetricsProducer(@Qualifier("metricsKafkaTemplate") KafkaTemplate<String, MetricsMessage> metricsKafkaTemplate) {
        this.metricsKafkaTemplate = metricsKafkaTemplate;
    }

    public boolean send(T message, MetricsType type) {
        Message<T> msg = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.KEY, type.name())
                .setHeader(KafkaHeaders.TOPIC, metricsKafkaTemplate.getDefaultTopic())
                .setHeader("type", type.name())
                .build();

        try {
            metricsKafkaTemplate.send(msg).get();
            log.info("Сообщение отправлено в Kafka: {}", message);
            return true;
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения в Kafka: {}", e.getMessage(), e);
            return false;
        }
    }

}
