package ru.t1.school.main_project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.t1.school.main_project.exception.type.EntityNotFoundException;
import ru.t1.school.main_project.exception.type.ValidationException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDetails handleEntityNotFound(EntityNotFoundException ex) {
        return new ErrorDetails(ex.getClass().getSimpleName(), ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleValidationException(ValidationException ex) {
        return new ErrorDetails(ex.getClass().getSimpleName(), ex.getMessage());
    }

    @Order(10_000)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDetails handleGeneralException(Exception ex) {
        log.error("Ошибка: {}", ex.getMessage(), ex);
        return new ErrorDetails("InternalServerError", "Произошла ошибка на сервере");
    }

}
