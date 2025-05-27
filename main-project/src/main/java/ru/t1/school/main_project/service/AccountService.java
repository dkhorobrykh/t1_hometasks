package ru.t1.school.main_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.main_project.aop.annotation.LogDataSourceError;
import ru.t1.school.main_project.model.Account;
import ru.t1.school.main_project.model.dto.AddAccountDto;
import ru.t1.school.main_project.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;

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
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));
    }

    @LogDataSourceError
    public Account createAccount(AddAccountDto dto) {
        if (dto.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        var client = clientService.getClientById(dto.getClientId());
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
            throw new IllegalArgumentException("Account not found with id: " + accountId);
        }
        accountRepository.deleteById(accountId);
    }

    @LogDataSourceError
    public Account updateAccount(Long accountId, AddAccountDto dto) {
        var account = getById(accountId);
        if (dto.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());
        return accountRepository.save(account);
    }
}
