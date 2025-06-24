package ru.t1.school.main_project.exception.type;

import java.util.UUID;

public class TransactionNotFoundException extends EntityNotFoundException{
    public TransactionNotFoundException(Long transactionId) {
        super("Транзакция с id %s не найдена".formatted(transactionId));
    }

    public TransactionNotFoundException(UUID transactionId) {
        super("Транзакция с id %s не найдена".formatted(transactionId));
    }
}
