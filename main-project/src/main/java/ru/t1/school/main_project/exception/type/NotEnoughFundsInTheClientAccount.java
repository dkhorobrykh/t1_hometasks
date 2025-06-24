package ru.t1.school.main_project.exception.type;

import java.util.UUID;

public class NotEnoughFundsInTheClientAccount extends ValidationException {
    public NotEnoughFundsInTheClientAccount(UUID accountId) {
        super("Недостаточно средств на счете %s".formatted(accountId));
    }
}
