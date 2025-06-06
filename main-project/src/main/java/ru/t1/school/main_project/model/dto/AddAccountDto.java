package ru.t1.school.main_project.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import ru.t1.school.main_project.model.Account;
import ru.t1.school.common.model.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Account}
 */
@Data
public class AddAccountDto implements Serializable {
    @NotNull
    private Long clientId;
    @NotNull
    private AccountType accountType;
    @PositiveOrZero
    private BigDecimal balance;
}