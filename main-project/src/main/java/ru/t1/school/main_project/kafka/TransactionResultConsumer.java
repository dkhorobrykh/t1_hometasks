package ru.t1.school.main_project.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.school.common.kafka.dto.TransactionResultMessage;
import ru.t1.school.main_project.service.TransactionService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionResultConsumer {

    private final TransactionService transactionService;

    @KafkaListener(
            id = "${kafka.consumer.group-id.transaction-result}",
            topics = "${kafka.topic.transaction-result}",
            containerFactory = "transactionResultListenerContainerFactory"
    )
    public void transactionListener(
            @Payload List<TransactionResultMessage> msgList,
            Acknowledgment ack
    ) {
        log.info("Начало обработки новых сообщений TransactionResultMessage");

        if (msgList != null) {
            msgList.forEach(msg -> {
                transactionService.processTransactionResult(msg);
                log.info("Результат {} успешно обработан", msg);
            });
        }

        ack.acknowledge();

        log.info("Окончание обработки новых сообщений TransactionResultMessage");
    }
}
