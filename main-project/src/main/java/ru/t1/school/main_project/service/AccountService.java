package ru.t1.school.main_project.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.common.model.AccountStatus;
import ru.t1.school.common.model.TransactionStatus;
import ru.t1.school.main_project.exception.type.AccountNotFoundException;
import ru.t1.school.main_project.exception.type.NegativeBalanceException;
import ru.t1.school.main_project.model.Account;
import ru.t1.school.main_project.model.Transaction;
import ru.t1.school.main_project.model.dto.AddAccountDto;
import ru.t1.school.main_project.repository.AccountRepository;
import ru.t1.school.the_best_starter.aop.annotation.LogDataSourceError;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ClientService clientService;

    @LogDataSourceError
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @LogDataSourceError
    public Account getById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    public Account getByAccountId(UUID accountId) {
        return accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @LogDataSourceError
    public Account createAccount(AddAccountDto dto) {
        if (dto.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeBalanceException();
        }
        var client = clientService.getById(dto.getClientId());
        var account = Account.builder()
                .accountType(dto.getAccountType())
                .balance(dto.getBalance())
                .client(client)
                .build();
        return accountRepository.save(account);
    }

    @LogDataSourceError
    public void deleteAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
        accountRepository.deleteById(accountId);
    }

    @LogDataSourceError
    public Account updateAccount(Long accountId, AddAccountDto dto) {
        var account = getById(accountId);
        if (dto.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeBalanceException();
        }
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());
        return accountRepository.save(account);
    }

    @Transactional
    public void withdrawFunds(Long accountId, Transaction transaction) {
        var account = getById(accountId);
        account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        accountRepository.saveAndFlush(account);
        log.info("С аккаунта {} успешно снято {} по транзакции {}", account.getAccountId(), transaction.getAmount(), transaction.getTransactionId());
    }

    public void freezeAmountAndReturnIt(UUID accountId, Transaction transaction) {
        var account = getByAccountId(accountId);

        if (transaction.getStatus().equals(TransactionStatus.ACCEPTED) || // транзакция успешна, средства списаны, замораживаем
                transaction.getStatus().equals(TransactionStatus.REQUESTED)) { // транзакция запрошена, средства списаны, замораживаем
            account.setBalance(account.getBalance().add(transaction.getAmount()));
            account.setFrozenAmount(account.getFrozenAmount().add(transaction.getAmount()));
        }

        accountRepository.saveAndFlush(account);
        log.info("Сумма {} по транзакции {} заморожена на счете {}", transaction.getAmount(), transaction.getTransactionId(), account.getAccountId());
    }

    public void blockAccount(UUID accountId) {
        var account = getByAccountId(accountId);
        account.setStatus(AccountStatus.BLOCKED);
        accountRepository.saveAndFlush(account);
        log.info("Счет {} заблокирован", account.getAccountId());
    }

    public void arrestAccount(UUID accountId) {
        var account = getByAccountId(accountId);
        account.setStatus(AccountStatus.ARRESTED);
        accountRepository.saveAndFlush(account);
        log.info("Счет {} арестован", account.getAccountId());
    }

    public void returnAmountToAccount(UUID accountId, Transaction transaction) {
        var account = getByAccountId(accountId);
        account.setBalance(account.getBalance().add(transaction.getAmount()));
        accountRepository.saveAndFlush(account);
        log.info("Сумма {} по транзакции {} возвращена на счет {}", transaction.getAmount(), transaction.getTransactionId(), account.getAccountId());
    }
}
