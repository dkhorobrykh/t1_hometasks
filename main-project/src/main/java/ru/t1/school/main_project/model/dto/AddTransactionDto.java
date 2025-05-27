package ru.t1.school.main_project.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import ru.t1.school.main_project.model.Transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link Transaction}
 */
@Data
public class AddTransactionDto implements Serializable {
    @NotNull
    private Long accountId;
    @NotNull
    private BigDecimal amount;
    private Instant datetime;
}