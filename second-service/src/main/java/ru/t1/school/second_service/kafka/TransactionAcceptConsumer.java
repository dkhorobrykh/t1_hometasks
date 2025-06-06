package ru.t1.school.second_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.school.common.kafka.dto.TransactionAcceptMessage;
import ru.t1.school.second_service.service.TransactionAcceptService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionAcceptConsumer {
    private final TransactionAcceptService transactionAcceptService;

    @KafkaListener(
            id = "${kafka.consumer.group-id.transaction-accept}",
            topics = "${kafka.topic.transaction-accept}",
            containerFactory = "transactionAcceptListenerContainerFactory"
    )
    public void transactionListener(
            @Payload List<TransactionAcceptMessage> msgList,
            Acknowledgment ack
    ) {
        log.info("Начало обработки новых сообщений TransactionAcceptMessage");

        if (msgList != null) {
            msgList.forEach(msg -> {
                transactionAcceptService.processTransaction(msg);
                log.info("Запрос на подтверждение транзакции {} успешно обработан", msg);
            });
        }

        ack.acknowledge();

        log.info("Окончание обработки новых сообщений TransactionAcceptMessage");
    }
}

