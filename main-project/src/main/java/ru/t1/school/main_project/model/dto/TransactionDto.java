package ru.t1.school.main_project.model.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link ru.t1.school.main_project.model.Transaction}
 */
@Data
public class TransactionDto implements Serializable {
    private Long id;
    private AccountDto account;
    private BigDecimal amount;
    private Instant datetime;
}