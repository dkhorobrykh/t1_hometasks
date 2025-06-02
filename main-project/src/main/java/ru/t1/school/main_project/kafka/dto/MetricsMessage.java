package ru.t1.school.main_project.kafka.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetricsMessage {
    private String signature;
    private String stacktrace;
    private Long duration;
    private String message;
}
