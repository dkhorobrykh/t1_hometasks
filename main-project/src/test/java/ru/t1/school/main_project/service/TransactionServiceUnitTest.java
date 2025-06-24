package ru.t1.school.main_project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import ru.t1.school.common.kafka.dto.TransactionAcceptMessage;
import ru.t1.school.common.kafka.dto.TransactionMessage;
import ru.t1.school.common.model.AccountStatus;
import ru.t1.school.common.model.ClientStatus;
import ru.t1.school.common.model.TransactionStatus;
import ru.t1.school.common.model.dto.ClientStatusResponse;
import ru.t1.school.main_project.BaseUnitTest;
import ru.t1.school.main_project.external.ExternalClientService;
import ru.t1.school.main_project.kafka.TransactionAcceptProducer;
import ru.t1.school.main_project.model.Account;
import ru.t1.school.main_project.model.Client;
import ru.t1.school.main_project.model.Transaction;
import ru.t1.school.main_project.model.dto.AddTransactionDto;
import ru.t1.school.main_project.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceUnitTest extends BaseUnitTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private ClientService clientService;

    @Mock
    private ExternalClientService externalClientService;

    @Mock
    private TransactionAcceptProducer transactionAcceptProducer;

    @InjectMocks
    private TransactionService transactionService;

    static Stream<AccountStatus> notOpenedAccountStatusProvider() {
        return Stream.of(
                AccountStatus.ARRESTED,
                AccountStatus.BLOCKED,
                AccountStatus.CLOSED
        );
    }

    @Test
    void createTransaction_WithCorrectData_ShouldPersistEntity() {
        // given
        var accountId = 111L;
        var timestamp = Instant.now();
        var dto = AddTransactionDto.builder()
                .accountId(accountId)
                .amount(new BigDecimal("123.45"))
                .timestamp(timestamp)
                .build();
        var account = Account.builder()
                .id(accountId)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(accountService.getById(accountId)).thenReturn(account);

        // when
        var result = transactionService.createTransaction(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccount().getId()).isEqualTo(accountId);
        assertThat(result.getAmount()).isEqualTo(new BigDecimal("123.45"));
        assertThat(result.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    void createTransaction_WithoutTimestamp_ShouldSetCurrentTimestamp() {
        // given
        var accountId = 111L;
        var dto = AddTransactionDto.builder()
                .accountId(accountId)
                .amount(new BigDecimal("123.45"))
                .build();
        var account = Account.builder()
                .id(accountId)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(accountService.getById(accountId)).thenReturn(account);

        // when
        var result = transactionService.createTransaction(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccount().getId()).isEqualTo(accountId);
        assertThat(result.getAmount()).isEqualTo(new BigDecimal("123.45"));
        assertThat(result.getTimestamp()).isCloseTo(Instant.now(), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void processTransaction_WithNullableClientStatusAndUnavailableService_shouldRejectTransaction() {
        // given
        var clientId = UUID.randomUUID();
        var accountId = UUID.randomUUID();

        var transactionMessage = TransactionMessage.builder()
                .clientId(clientId)
                .accountId(accountId)
                .amount(new BigDecimal("123.45"))
                .timestamp(Instant.now())
                .build();

        var clientWithoutStatus = Client.builder()
                .build();

        var externalClientServiceResponse = ClientStatusResponse.builder()
                .status(null)
                .clientId(clientId)
                .build();

        var account = Account.builder()
                .status(AccountStatus.OPEN)
                .build();

        when(clientService.getByClientId(clientId)).thenReturn(clientWithoutStatus);
        when(accountService.getByAccountId(accountId)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        var transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        // when
        transactionService.processTransaction(transactionMessage);
        verify(transactionRepository).saveAndFlush(transactionCaptor.capture());
        var savedTransaction = transactionCaptor.getValue();

        // then
        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getStatus()).isEqualTo(TransactionStatus.REJECTED);
    }

    @Test
    void processTransaction_WithALotOfRejectedTransactionsByClient_shouldRejectTransactionAndArrestAccount() {
        // given
        var clientId = UUID.randomUUID();
        var accountId = UUID.randomUUID();

        var transactionMessage = TransactionMessage.builder()
                .clientId(clientId)
                .accountId(accountId)
                .amount(new BigDecimal("123.45"))
                .timestamp(Instant.now())
                .build();

        var clientWithoutStatus = Client.builder()
                .status(ClientStatus.ACTIVE)
                .build();

        var externalClientServiceResponse = ClientStatusResponse.builder()
                .status(ClientStatus.ACTIVE)
                .clientId(clientId)
                .build();

        var account = Account.builder()
                .status(AccountStatus.OPEN)
                .build();

        when(clientService.getByClientId(clientId)).thenReturn(clientWithoutStatus);
        when(accountService.getByAccountId(accountId)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.getNumberOfRejectedTransactionsByClientIdAndStatus(clientId, TransactionStatus.REJECTED)).thenReturn(10000);
        ReflectionTestUtils.setField(transactionService, "maxNumberOfRejectedTransactionsByClient", 1);

        var transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        // when
        transactionService.processTransaction(transactionMessage);
        verify(transactionRepository).saveAndFlush(transactionCaptor.capture());
        verify(accountService, times(1)).arrestAccount(accountId);
        var savedTransaction = transactionCaptor.getValue();

        // then
        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getStatus()).isEqualTo(TransactionStatus.REJECTED);
    }

    @Test
    void processTransaction_WithBlockedClient_shouldRejectTransactionAndBlockAccount() {
        // given
        var clientId = UUID.randomUUID();
        var accountId = UUID.randomUUID();

        var transactionMessage = TransactionMessage.builder()
                .clientId(clientId)
                .accountId(accountId)
                .amount(new BigDecimal("123.45"))
                .timestamp(Instant.now())
                .build();

        var clientWithoutStatus = Client.builder()
                .build();

        var externalClientServiceResponse = ClientStatusResponse.builder()
                .status(ClientStatus.BLOCKED)
                .clientId(clientId)
                .build();

        var account = Account.builder()
                .status(AccountStatus.OPEN)
                .build();

        when(clientService.getByClientId(clientId)).thenReturn(clientWithoutStatus);
        when(accountService.getByAccountId(accountId)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        var transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        // when
        transactionService.processTransaction(transactionMessage);
        verify(transactionRepository).saveAndFlush(transactionCaptor.capture());
        var savedTransaction = transactionCaptor.getValue();

        // then
        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getStatus()).isEqualTo(TransactionStatus.REJECTED);
    }

    @ParameterizedTest
    @MethodSource("notOpenedAccountStatusProvider")
    void processTransaction_WithNotOpenedAccountStatus_shouldRejectTransaction(AccountStatus accountStatus) {
        // given
        var clientId = UUID.randomUUID();
        var accountId = UUID.randomUUID();

        var transactionMessage = TransactionMessage.builder()
                .clientId(clientId)
                .accountId(accountId)
                .amount(new BigDecimal("123.45"))
                .timestamp(Instant.now())
                .build();

        var clientWithStatus = Client.builder()
                .status(ClientStatus.ACTIVE)
                .build();

        var account = Account.builder()
                .status(accountStatus)
                .build();

        when(clientService.getByClientId(clientId)).thenReturn(clientWithStatus);
        when(accountService.getByAccountId(accountId)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.getNumberOfRejectedTransactionsByClientIdAndStatus(clientId, TransactionStatus.REJECTED)).thenReturn(0);
        var transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        ReflectionTestUtils.setField(transactionService, "maxNumberOfRejectedTransactionsByClient", 3);

        // when
        transactionService.processTransaction(transactionMessage);
        verify(transactionRepository).saveAndFlush(transactionCaptor.capture());
        var savedTransaction = transactionCaptor.getValue();

        // then
        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getStatus()).isEqualTo(TransactionStatus.REJECTED);
    }

    @ParameterizedTest
    @MethodSource("notOpenedAccountStatusProvider")
    void processTransaction_WithCorrectData_ShouldProcessTransaction() {
        // given
        var clientId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var timestamp = Instant.now();

        var transactionMessage = TransactionMessage.builder()
                .clientId(clientId)
                .accountId(accountId)
                .amount(new BigDecimal("123.45"))
                .timestamp(timestamp)
                .build();

        var client = Client.builder()
                .status(ClientStatus.ACTIVE)
                .clientId(clientId)
                .build();

        var account = Account.builder()
                .id(111L)
                .accountId(accountId)
                .status(AccountStatus.OPEN)
                .balance(new BigDecimal("1000.00"))
                .client(client)
                .build();

        when(clientService.getByClientId(clientId)).thenReturn(client);
        when(accountService.getByAccountId(accountId)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.getNumberOfRejectedTransactionsByClientIdAndStatus(clientId, TransactionStatus.REJECTED)).thenReturn(0);
        var transactionAcceptMsg = ArgumentCaptor.forClass(TransactionAcceptMessage.class);
        ReflectionTestUtils.setField(transactionService, "maxNumberOfRejectedTransactionsByClient", 3);

        // when
        transactionService.processTransaction(transactionMessage);
        verify(transactionAcceptProducer).send(transactionAcceptMsg.capture());
        verify(accountService, times(1)).withdrawFunds(anyLong(), any(Transaction.class));
        var sentMsg = transactionAcceptMsg.getValue();

        // then
        assertThat(sentMsg).isNotNull();
        assertThat(sentMsg.getClientId()).isEqualTo(clientId);
        assertThat(sentMsg.getAccountId()).isEqualTo(accountId);
        assertThat(sentMsg.getTimestamp()).isEqualTo(timestamp);
        assertThat(sentMsg.getTransactionAmount()).isEqualTo(transactionMessage.getAmount());
        assertThat(sentMsg.getAccountBalance()).isEqualTo(account.getBalance());
    }
}
