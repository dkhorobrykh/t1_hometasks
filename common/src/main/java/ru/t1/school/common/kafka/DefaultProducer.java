package ru.t1.school.common.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

@Slf4j
public abstract class DefaultProducer {
    public <T> boolean send(KafkaTemplate<String, T> template, Message<T> msg) {
        try {
            template.send(msg).get();
            log.info("Сообщение отправлено в Kafka: {}", msg);
            return true;
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения в Kafka: {}", e.getMessage(), e);
            return false;
        }
    }
}
