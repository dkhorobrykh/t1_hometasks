package ru.t1.school.second_service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.t1.school.common.kafka.DefaultProducer;
import ru.t1.school.common.kafka.dto.MetricsType;
import ru.t1.school.common.kafka.dto.TransactionResultMessage;

@Slf4j
@Component
public class TransactionResultProducer extends DefaultProducer {
    private final KafkaTemplate<String, TransactionResultMessage> transactionResultKafkaTemplate;

    public TransactionResultProducer(@Qualifier("transactionResultKafkaTemplate") KafkaTemplate<String, TransactionResultMessage> transactionResultKafkaTemplate) {
        this.transactionResultKafkaTemplate = transactionResultKafkaTemplate;
    }

    public boolean send(TransactionResultMessage message) {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, transactionResultKafkaTemplate.getDefaultTopic())
                .build();

        return send(transactionResultKafkaTemplate, msg);
    }
}
