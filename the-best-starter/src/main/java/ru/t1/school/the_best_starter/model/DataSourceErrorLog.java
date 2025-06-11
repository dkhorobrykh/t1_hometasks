package ru.t1.school.the_best_starter.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "data_source_error_log",
        schema = "public"
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataSourceErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stack_trace", nullable = false)
    private String stackTrace;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "signature", nullable = false)
    private String signature;
}
