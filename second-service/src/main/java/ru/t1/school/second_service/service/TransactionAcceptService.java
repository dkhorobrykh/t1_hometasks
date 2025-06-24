package ru.t1.school.second_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.school.common.kafka.dto.TransactionAcceptMessage;
import ru.t1.school.common.kafka.dto.TransactionResultMessage;
import ru.t1.school.common.model.TransactionStatus;
import ru.t1.school.second_service.kafka.TransactionResultProducer;
import ru.t1.school.second_service.model.TransactionHistory;
import ru.t1.school.second_service.repository.TransactionHistoryRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionAcceptService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionResultProducer transactionResultProducer;
    @Value("${transactions.max-number-of-transactions-per-account-at-period:3}")
    private Integer maxNumberOfTransactionsPerAccountAtPeriod;
    @Value("${transactions.time-period-in-seconds:600}")
    private Integer timePeriodInSeconds;

    public void processTransaction(TransactionAcceptMessage msg) {
        var history = saveTransactionHistory(msg);

        var lastTransactionsByClientAndAccount = transactionHistoryRepository.findAllLastTransactionsByClientAndAccount(msg.getClientId(), msg.getAccountId(), Instant.now().minus(timePeriodInSeconds, ChronoUnit.SECONDS), Instant.now());

        if (msg.getTransactionAmount().compareTo(msg.getAccountBalance()) > 0) {
            sendResults(msg.getAccountId(), msg.getTransactionId(), TransactionStatus.REJECTED);
            history.setStatus(TransactionStatus.REJECTED);
            transactionHistoryRepository.saveAndFlush(history);
            return;
        }

        if (lastTransactionsByClientAndAccount.size() > maxNumberOfTransactionsPerAccountAtPeriod) {
            blockLastTransactions(lastTransactionsByClientAndAccount);
            return;
        }

        sendResults(msg.getAccountId(), msg.getTransactionId(), TransactionStatus.ACCEPTED);
        history.setStatus(TransactionStatus.ACCEPTED);
        transactionHistoryRepository.saveAndFlush(history);
    }

    private TransactionHistory saveTransactionHistory(TransactionAcceptMessage msg) {
        var history = TransactionHistory.builder()
                .clientId(msg.getClientId())
                .accountId(msg.getAccountId())
                .transactionId(msg.getTransactionId())
                .timestamp(msg.getTimestamp())
                .transactionAmount(msg.getTransactionAmount())
                .accountBalance(msg.getAccountBalance())
                .status(TransactionStatus.REQUESTED)
                .build();

        return transactionHistoryRepository.saveAndFlush(history);
    }

    private void sendResults(UUID accountId, UUID transactionId, TransactionStatus status) {
        var msg = TransactionResultMessage.builder()
                .accountId(accountId)
                .transactionId(transactionId)
                .status(status)
                .build();
        transactionResultProducer.send(msg);
    }

    private void blockLastTransactions(List<TransactionHistory> transactions) {
        int limit = 0;
        for (var transaction : transactions) {
            if (limit >= maxNumberOfTransactionsPerAccountAtPeriod) {
                return;
            }

            if (transaction.getStatus() == null || !transaction.getStatus().equals(TransactionStatus.BLOCKED)) {
                sendResults(transaction.getAccountId(), transaction.getTransactionId(), TransactionStatus.BLOCKED);
                transaction.setStatus(TransactionStatus.BLOCKED);
                transactionHistoryRepository.saveAndFlush(transaction);
            }
            limit++;
        }
    }
}
