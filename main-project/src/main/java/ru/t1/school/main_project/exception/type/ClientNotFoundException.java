package ru.t1.school.main_project.exception.type;

import java.util.UUID;

public class ClientNotFoundException extends EntityNotFoundException {
    public ClientNotFoundException(Long clientId) {
        super("Пользователь с id %s не найден".formatted(clientId));
    }

    public ClientNotFoundException(UUID clientId) {
        super("Пользователь с id %s не найден".formatted(clientId));
    }
}