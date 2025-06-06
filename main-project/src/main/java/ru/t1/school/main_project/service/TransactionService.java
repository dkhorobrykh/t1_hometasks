package ru.t1.school.main_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.common.kafka.dto.TransactionMessage;
import ru.t1.school.common.kafka.dto.TransactionResultMessage;
import ru.t1.school.main_project.aop.annotation.LogDataSourceError;
import ru.t1.school.main_project.exception.type.TransactionNotFoundException;
import ru.t1.school.main_project.kafka.TransactionAcceptProducer;
import ru.t1.school.common.kafka.dto.TransactionAcceptMessage;
import ru.t1.school.common.model.AccountStatus;
import ru.t1.school.main_project.model.Transaction;
import ru.t1.school.common.model.TransactionStatus;
import ru.t1.school.main_project.model.dto.AddTransactionDto;
import ru.t1.school.main_project.repository.TransactionRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final TransactionAcceptProducer transactionAcceptProducer;

    @LogDataSourceError
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    @LogDataSourceError
    public Transaction getById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    @LogDataSourceError
    public Transaction createTransaction(AddTransactionDto dto) {
        var account = accountService.getById(dto.getAccountId());
        var transaction = Transaction.builder()
                .account(account)
                .amount(dto.getAmount())
                .timestamp(dto.getTimestamp() == null ? Instant.now() : dto.getTimestamp())
                .build();
        return transactionRepository.save(transaction);
    }

    @LogDataSourceError
    public void deleteTransaction(Long transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException(transactionId);
        }
        transactionRepository.deleteById(transactionId);
    }

    @LogDataSourceError
    public Transaction updateTransaction(Long transactionId, AddTransactionDto dto) {
        var transaction = getById(transactionId);
        var account = accountService.getById(dto.getAccountId());
        transaction.setAccount(account);
        transaction.setAmount(dto.getAmount());
        transaction.setTimestamp(dto.getTimestamp() == null ? transaction.getTimestamp() : dto.getTimestamp());
        return transactionRepository.save(transaction);
    }

    public Transaction saveTransaction(TransactionMessage transactionMessage, TransactionStatus status) {
        var account = accountService.getByAccountId(transactionMessage.getAccountId());
        var transaction = Transaction.builder()
                .account(account)
                .amount(transactionMessage.getAmount())
                .timestamp(Optional.ofNullable(transactionMessage.getTimestamp()).orElse(Instant.now()))
                .status(TransactionStatus.REQUESTED)
                .build();
        var savedTransaction = transactionRepository.save(transaction);
        log.info("Транзакция {} сохранена", savedTransaction);
        return savedTransaction;
    }

    public void processTransaction(TransactionMessage transactionMessage) {
        var account = accountService.getByAccountId(transactionMessage.getAccountId());
        if (!account.getStatus().equals(AccountStatus.OPEN)) {
            log.info("Счет {} не в статусе OPEN, транзакция {} не может быть обработана", account.getAccountId(), transactionMessage);
            return;
        }
        var savedTransaction = saveTransaction(transactionMessage, TransactionStatus.REQUESTED);

        accountService.withdrawFunds(account.getId(), savedTransaction);

        var transactionAcceptMsg = TransactionAcceptMessage.builder()
                .clientId(account.getClient().getClientId())
                .accountId(account.getAccountId())
                .transactionId(savedTransaction.getTransactionId())
                .timestamp(savedTransaction.getTimestamp())
                .transactionAmount(savedTransaction.getAmount())
                .accountBalance(account.getBalance())
                .build();
        transactionAcceptProducer.send(transactionAcceptMsg);
    }

    public Transaction getByTransactionId(UUID transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    public void processTransactionResult(TransactionResultMessage msg) {
        if (msg.getStatus().equals(TransactionStatus.ACCEPTED)) {
            var transaction = getByTransactionId(msg.getTransactionId());
            transaction.setStatus(TransactionStatus.ACCEPTED);
            transactionRepository.saveAndFlush(transaction);
        } else if (msg.getStatus().equals(TransactionStatus.BLOCKED)) {
            var transaction = getByTransactionId(msg.getTransactionId());
            accountService.blockAccount(msg.getAccountId());
            accountService.freezeAmountAndReturnIt(msg.getAccountId(), transaction);
            transaction.setStatus(TransactionStatus.BLOCKED);
            transactionRepository.saveAndFlush(transaction);
        } else if (msg.getStatus().equals(TransactionStatus.REJECTED)) {
            var transaction = getByTransactionId(msg.getTransactionId());
            transaction.setStatus(TransactionStatus.REJECTED);
            transactionRepository.saveAndFlush(transaction);
            accountService.returnAmountToAccount(msg.getAccountId(), transaction);
        }
    }
}
