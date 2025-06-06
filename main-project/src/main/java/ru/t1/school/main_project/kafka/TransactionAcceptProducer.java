package ru.t1.school.main_project.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.t1.school.common.kafka.DefaultProducer;
import ru.t1.school.common.kafka.dto.TransactionAcceptMessage;

@Slf4j
@Component
public class TransactionAcceptProducer extends DefaultProducer {
    private final KafkaTemplate<String, TransactionAcceptMessage> transactionAcceptKafkaTemplate;

    public TransactionAcceptProducer(@Qualifier("transactionAcceptKafkaTemplate") KafkaTemplate<String, TransactionAcceptMessage> transactionAcceptMessageKafkaProducer) {
        this.transactionAcceptKafkaTemplate = transactionAcceptMessageKafkaProducer;
    }

    public boolean send(TransactionAcceptMessage message) {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, transactionAcceptKafkaTemplate.getDefaultTopic())
                .build();

        return send(transactionAcceptKafkaTemplate, msg);
    }
}
