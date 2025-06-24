package ru.t1.school.main_project.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.school.main_project.model.Transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link Transaction}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddTransactionDto implements Serializable {
    @NotNull
    private Long accountId;
    @NotNull
    private BigDecimal amount;
    private Instant timestamp;
}