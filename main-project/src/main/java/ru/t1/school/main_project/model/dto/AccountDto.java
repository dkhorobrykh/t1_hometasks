package ru.t1.school.main_project.model.dto;

import lombok.Data;
import lombok.Value;
import ru.t1.school.main_project.model.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link ru.t1.school.main_project.model.Account}
 */
@Data
public class AccountDto implements Serializable {
    private Long id;
    private ClientDto client;
    private AccountType accountType;
    private BigDecimal balance;
}