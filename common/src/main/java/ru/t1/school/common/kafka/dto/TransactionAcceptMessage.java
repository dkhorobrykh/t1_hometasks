package ru.t1.school.common.kafka.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionAcceptMessage {
    @NotNull
    private UUID clientId;
    @NotNull
    private UUID accountId;
    @NotNull
    private UUID transactionId;
    private Instant timestamp;
    private BigDecimal transactionAmount;
    private BigDecimal accountBalance;
}
