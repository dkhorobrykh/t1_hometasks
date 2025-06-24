package ru.t1.school.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.school.common.model.TransactionStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResultMessage {
    private UUID accountId;
    private UUID transactionId;
    private TransactionStatus status;
}
