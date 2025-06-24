package ru.t1.school.main_project.exception.type;

import java.util.UUID;

public class AccountNotFoundException extends EntityNotFoundException {
    public AccountNotFoundException(Long accountId) {
        super("Счет с id %s не найден".formatted(accountId));
    }

    public AccountNotFoundException(UUID accountId) {
        super("Счет с id %s не найден".formatted(accountId));
    }
}