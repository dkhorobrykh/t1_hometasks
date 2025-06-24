package ru.t1.school.main_project.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.t1.school.common.model.AccountStatus;
import ru.t1.school.common.model.AccountType;
import ru.t1.school.common.model.ClientStatus;
import ru.t1.school.main_project.BaseIntegrationTest;
import ru.t1.school.main_project.model.Account;
import ru.t1.school.main_project.model.Client;
import ru.t1.school.main_project.repository.AccountRepository;
import ru.t1.school.main_project.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountServiceIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void removeArrestFromAccounts_WithOnlyArrestedAccounts_ShouldRemoveArrest() {
        // given
        Client client = Client.builder()
                .clientId(UUID.fromString("b3bf1876-197a-46fe-b4a4-089de437df57"))
                .firstName("John")
                .lastName("Doe")
                .middleName(null)
                .status(ClientStatus.ACTIVE)
                .build();

        client = clientRepository.saveAndFlush(client);

        List<Account> accounts = List.of(
                Account.builder()
                        .accountId(UUID.fromString("b3bf1875-197a-46fe-b4a4-089de437df57"))
                        .client(client)
                        .accountType(AccountType.DEBIT)
                        .frozenAmount(BigDecimal.ZERO)
                        .balance(new BigDecimal("1000.00"))
                        .status(AccountStatus.ARRESTED)
                        .build(),
                Account.builder()
                        .accountId(UUID.fromString("a67ddd93-c43a-4d87-842b-f772d02a28fe"))
                        .client(client)
                        .accountType(AccountType.DEBIT)
                        .frozenAmount(BigDecimal.ZERO)
                        .balance(new BigDecimal("1000.00"))
                        .status(AccountStatus.ARRESTED)
                        .build(),
                Account.builder()
                        .accountId(UUID.fromString("c8e14b0d-e43e-424a-8606-d7141939b392"))
                        .client(client)
                        .accountType(AccountType.DEBIT)
                        .frozenAmount(BigDecimal.ZERO)
                        .balance(new BigDecimal("1000.00"))
                        .status(AccountStatus.ARRESTED)
                        .build()
        );

        accountRepository.deleteAll();
        accountRepository.saveAllAndFlush(accounts);

        // when
        accountService.removeArrestFromAccounts();
        var accountsAfterRemoval = accountRepository.findAll();

        // then
        assertThat(accountsAfterRemoval).hasSize(3);
        assertThat(accountsAfterRemoval.get(0).getStatus()).isEqualTo(AccountStatus.OPEN);
        assertThat(accountsAfterRemoval.get(1).getStatus()).isEqualTo(AccountStatus.OPEN);
        assertThat(accountsAfterRemoval.get(2).getStatus()).isEqualTo(AccountStatus.OPEN);
    }

    @Test
    void removeArrestFromAccounts_WithDifferentAccounts_ShouldRemoveArrestFromOnlyArrested() {
        // given
        Client client = Client.builder()
                .clientId(UUID.fromString("b3bf1870-197a-46fe-b4a4-089de437df57"))
                .firstName("John")
                .lastName("Doe")
                .middleName(null)
                .status(ClientStatus.ACTIVE)
                .build();

        client = clientRepository.saveAndFlush(client);

        List<Account> accounts = List.of(
                Account.builder()
                        .accountId(UUID.fromString("b3bf1875-197a-46fe-b4a4-089de437df57"))
                        .client(client)
                        .accountType(AccountType.DEBIT)
                        .frozenAmount(BigDecimal.ZERO)
                        .balance(new BigDecimal("1000.00"))
                        .status(AccountStatus.OPEN)
                        .build(),
                Account.builder()
                        .accountId(UUID.fromString("a67ddd93-c43a-4d87-842b-f772d02a28fe"))
                        .client(client)
                        .accountType(AccountType.DEBIT)
                        .frozenAmount(BigDecimal.ZERO)
                        .balance(new BigDecimal("1000.00"))
                        .status(AccountStatus.ARRESTED)
                        .build(),
                Account.builder()
                        .accountId(UUID.fromString("c8e14b0d-e43e-424a-8606-d7141939b392"))
                        .client(client)
                        .accountType(AccountType.DEBIT)
                        .frozenAmount(BigDecimal.ZERO)
                        .balance(new BigDecimal("1000.00"))
                        .status(AccountStatus.CLOSED)
                        .build()
        );

        accountRepository.deleteAll();
        accountRepository.saveAllAndFlush(accounts);

        // when
        accountService.removeArrestFromAccounts();
        var firstAccount = accountRepository.findByAccountId(UUID.fromString("b3bf1875-197a-46fe-b4a4-089de437df57")).orElseThrow();
        var secondAccount = accountRepository.findByAccountId(UUID.fromString("a67ddd93-c43a-4d87-842b-f772d02a28fe")).orElseThrow();
        var thirdAccount = accountRepository.findByAccountId(UUID.fromString("c8e14b0d-e43e-424a-8606-d7141939b392")).orElseThrow();

        // then
        assertThat(firstAccount.getStatus()).isEqualTo(AccountStatus.OPEN);
        assertThat(secondAccount.getStatus()).isEqualTo(AccountStatus.OPEN);
        assertThat(thirdAccount.getStatus()).isEqualTo(AccountStatus.CLOSED);
    }
}
