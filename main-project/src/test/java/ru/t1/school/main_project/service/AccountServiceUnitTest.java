package ru.t1.school.main_project.service;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.t1.school.common.model.AccountStatus;
import ru.t1.school.common.model.AccountType;
import ru.t1.school.common.model.TransactionStatus;
import ru.t1.school.main_project.BaseUnitTest;
import ru.t1.school.main_project.exception.type.NegativeBalanceException;
import ru.t1.school.main_project.model.Account;
import ru.t1.school.main_project.model.Client;
import ru.t1.school.main_project.model.Transaction;
import ru.t1.school.main_project.model.dto.AddAccountDto;
import ru.t1.school.main_project.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceUnitTest extends BaseUnitTest {
    @Mock
    private ClientService clientService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    static Stream<BigDecimal> correctBalanceProvider() {
        return Stream.of(new BigDecimal("123.34"), BigDecimal.ZERO);
    }

    static Stream<Transaction> completedTransactionProvider() {
        return Stream.of(
                Transaction.builder()
                        .status(TransactionStatus.ACCEPTED)
                        .amount(new BigDecimal("1000.00"))
                        .build(),
                Transaction.builder()
                        .status(TransactionStatus.REQUESTED)
                        .amount(new BigDecimal("1000.00"))
                        .build()
        );
    }

    static Stream<Transaction> incompletedTransactionProvider() {
        return Stream.of(
                Transaction.builder()
                        .status(TransactionStatus.REJECTED)
                        .amount(new BigDecimal("1000.00"))
                        .build(),
                Transaction.builder()
                        .status(TransactionStatus.BLOCKED)
                        .amount(new BigDecimal("1000.00"))
                        .build(),
                Transaction.builder()
                        .status(TransactionStatus.CANCELLED)
                        .amount(new BigDecimal("1000.00"))
                        .build()
        );
    }

    @ParameterizedTest
    @MethodSource("correctBalanceProvider")
    void createAccount_WithZeroOrPositiveBalance_ShouldPersistCorrectEntity(BigDecimal balance) {
        // given
        var clientId = 111L;
        var mockedClient = new Client();

        var dto = AddAccountDto.builder()
                .clientId(clientId)
                .accountType(AccountType.DEBIT)
                .balance(balance)
                .build();

        when(clientService.getById(clientId)).thenReturn(mockedClient);
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        // when
        var result = accountService.createAccount(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(AccountStatus.OPEN);
        assertThat(result.getAccountType()).isEqualTo(dto.getAccountType());
        assertThat(result.getClient()).isEqualTo(mockedClient);
        assertThat(result.getFrozenAmount()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getBalance()).isEqualTo(balance);
    }

    @Test
    void createAccount_WithNegativeBalance_ShouldThrowException() {
        // given
        var clientId = 111L;

        var dto = AddAccountDto.builder()
                .clientId(clientId)
                .accountType(AccountType.DEBIT)
                .balance(new BigDecimal("-123.45"))
                .build();

        // when
        assertThatThrownBy(() -> accountService.createAccount(dto)).isInstanceOf(NegativeBalanceException.class);
    }

    @Test
    void withdrawFunds_WithCorrectData_ShouldSubtractAccountBalance() {
        // given
        var accountId = 111L;
        var account = Account.builder()
                .balance(new BigDecimal("10000.00"))
                .build();
        var transaction = Transaction.builder()
                .amount(new BigDecimal("1000.00"))
                .build();
        var copiedAccount = SerializationUtils.clone(account);

        when(accountRepository.saveAndFlush(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(copiedAccount));
        var accountCaptor = ArgumentCaptor.forClass(Account.class);

        // when
        accountService.withdrawFunds(accountId, transaction);
        verify(accountRepository).saveAndFlush(accountCaptor.capture());
        var savedAccount = accountCaptor.getValue();

        // then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualTo(new BigDecimal("9000.00"));
    }

    @ParameterizedTest
    @MethodSource("completedTransactionProvider")
    void freezeAmountAndReturnIt_WithCompletedTransaction_ShouldAddBalanceAndAddFrozenAmount(Transaction transaction) {
        // given
        var accountId = UUID.randomUUID();
        var account = Account.builder()
                .balance(new BigDecimal("1000.00"))
                .frozenAmount(BigDecimal.ZERO)
                .build();
        var copiedAccount = SerializationUtils.clone(account);

        when(accountRepository.findByAccountId(accountId)).thenReturn(Optional.of(copiedAccount));
        when(accountRepository.saveAndFlush(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        var accountCaptor = ArgumentCaptor.forClass(Account.class);

        // when
        accountService.freezeAmountAndReturnIt(accountId, transaction);
        verify(accountRepository).saveAndFlush(accountCaptor.capture());
        var savedAccount = accountCaptor.getValue();

        // then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualTo(account.getBalance().add(transaction.getAmount()));
        assertThat(savedAccount.getFrozenAmount()).isEqualTo(transaction.getAmount());
    }

    @ParameterizedTest
    @MethodSource("incompletedTransactionProvider")
    void freezeAmountAndReturnIt_WithIncompletedTransaction_ShouldDoNothing(Transaction transaction) {
        // given
        var accountId = UUID.randomUUID();
        var account = Account.builder()
                .balance(new BigDecimal("1000.00"))
                .frozenAmount(BigDecimal.ZERO)
                .build();
        var copiedAccount = SerializationUtils.clone(account);

        when(accountRepository.findByAccountId(accountId)).thenReturn(Optional.of(copiedAccount));
        when(accountRepository.saveAndFlush(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        var accountCaptor = ArgumentCaptor.forClass(Account.class);

        // when
        accountService.freezeAmountAndReturnIt(accountId, transaction);
        verify(accountRepository).saveAndFlush(accountCaptor.capture());
        var savedAccount = accountCaptor.getValue();

        // then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualTo(account.getBalance());
        assertThat(savedAccount.getFrozenAmount()).isEqualTo(account.getFrozenAmount());
    }

    @Test
    void blockAccount_WithCorrectData_ShouldSetBlockedStatus() {
        // given
        var accountId = UUID.randomUUID();
        var account = Account.builder()
                .status(AccountStatus.OPEN)
                .build();
        var copiedAccount = SerializationUtils.clone(account);

        when(accountRepository.findByAccountId(accountId)).thenReturn(Optional.of(copiedAccount));
        when(accountRepository.saveAndFlush(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        var accountCaptor = ArgumentCaptor.forClass(Account.class);

        // when
        accountService.blockAccount(accountId);
        verify(accountRepository).saveAndFlush(accountCaptor.capture());
        var savedAccount = accountCaptor.getValue();

        // then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getStatus()).isEqualTo(AccountStatus.BLOCKED);
    }

    @Test
    void arrestAccount_WithCorrectData_ShouldSetArrestedStatus() {
        // given
        var accountId = UUID.randomUUID();
        var account = Account.builder()
                .status(AccountStatus.OPEN)
                .build();
        var copiedAccount = SerializationUtils.clone(account);

        when(accountRepository.findByAccountId(accountId)).thenReturn(Optional.of(copiedAccount));
        when(accountRepository.saveAndFlush(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        var accountCaptor = ArgumentCaptor.forClass(Account.class);

        // when
        accountService.arrestAccount(accountId);
        verify(accountRepository).saveAndFlush(accountCaptor.capture());
        var savedAccount = accountCaptor.getValue();

        // then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getStatus()).isEqualTo(AccountStatus.ARRESTED);
    }

    @Test
    void returnAmountToAccount_WithCorrectData_ShouldIncreaseAccountBalance() {
        // given
        var accountId = UUID.randomUUID();
        var account = Account.builder()
                .balance(new BigDecimal("1000.00"))
                .build();
        var transaction = Transaction.builder()
                .amount(new BigDecimal("500"))
                .build();
        var copiedAccount = SerializationUtils.clone(account);

        when(accountRepository.findByAccountId(accountId)).thenReturn(Optional.of(copiedAccount));
        when(accountRepository.saveAndFlush(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        var accountCaptor = ArgumentCaptor.forClass(Account.class);

        // when
        accountService.returnAmountToAccount(accountId, transaction);
        verify(accountRepository).saveAndFlush(accountCaptor.capture());
        var savedAccount = accountCaptor.getValue();

        // then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualTo(account.getBalance().add(transaction.getAmount()));
    }
}
