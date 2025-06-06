package ru.t1.school.common.kafka.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionMessage {
    @NotNull
    private UUID clientId;
    @NotNull
    private UUID accountId;
    @NotNull
    private BigDecimal amount;
    private Instant timestamp;
}
