package ru.t1.school.main_project.exception.type;

public class NegativeBalanceException extends ValidationException {
    public NegativeBalanceException() {
        super("Баланс не может быть отрицательным");
    }
}
