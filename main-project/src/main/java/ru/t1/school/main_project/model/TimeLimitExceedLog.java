package ru.t1.school.main_project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "time_limit_exceed_log",
        schema = "public"
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeLimitExceedLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_datetime", nullable = false)
    private Instant startDatetime;

    @Column(name = "end_datetime", nullable = false)
    private Instant endDatetime;

    @Column(name = "signature", nullable = false)
    private String signature;

    @Column(name = "duration", nullable = false)
    private Long duration;
}
