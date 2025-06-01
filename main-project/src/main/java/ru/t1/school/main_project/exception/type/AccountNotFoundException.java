package ru.t1.school.main_project.exception.type;

public class AccountNotFoundException extends EntityNotFoundException {
    public AccountNotFoundException(Long accountId) {
        super("Счет с id %s не найден".formatted(accountId));
    }
}