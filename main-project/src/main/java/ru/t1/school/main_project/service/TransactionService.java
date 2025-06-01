package ru.t1.school.main_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.main_project.aop.annotation.LogDataSourceError;
import ru.t1.school.main_project.exception.type.TransactionNotFoundException;
import ru.t1.school.main_project.model.dto.AddTransactionDto;
import ru.t1.school.main_project.model.Transaction;
import ru.t1.school.main_project.repository.TransactionRepository;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

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
                .datetime(dto.getDatetime() == null ? Instant.now() : dto.getDatetime())
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
        transaction.setDatetime(dto.getDatetime() == null ? transaction.getDatetime() : dto.getDatetime());
        return transactionRepository.save(transaction);
    }
}
