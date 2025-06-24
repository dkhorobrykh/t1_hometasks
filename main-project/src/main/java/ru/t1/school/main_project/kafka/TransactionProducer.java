package ru.t1.school.main_project.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.t1.school.common.kafka.DefaultProducer;
import ru.t1.school.common.kafka.dto.TransactionMessage;

@Slf4j
@Component
public class TransactionProducer extends DefaultProducer {
    private final KafkaTemplate<String, TransactionMessage> transactionKafkaTemplate;

    public TransactionProducer(@Qualifier("transactionKafkaTemplate") KafkaTemplate<String, TransactionMessage> transactionKafkaTemplate) {
        this.transactionKafkaTemplate = transactionKafkaTemplate;
    }

    public boolean send(TransactionMessage message) {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, transactionKafkaTemplate.getDefaultTopic())
                .build();

        return send(transactionKafkaTemplate, msg);
    }
}
