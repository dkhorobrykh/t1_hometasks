package ru.t1.school.main_project.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetails {
    private String error;
    private String message;
}
