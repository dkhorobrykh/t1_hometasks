package ru.t1.school.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricsMessage {
    private String signature;
    private String stacktrace;
    private Long duration;
    private String message;
}
