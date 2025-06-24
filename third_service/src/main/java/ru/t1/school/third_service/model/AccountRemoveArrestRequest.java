package ru.t1.school.third_service.model;

import jakarta.persistence.*;
import lombok.*;
import ru.t1.school.common.model.AccountStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "account_remove_arrest_request",
        schema = "public"
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountRemoveArrestRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "account_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "timestamp", nullable = false)
    @Builder.Default
    private Instant timestamp = Instant.now();

    @Column(name = "result")
    private Boolean result;

    @Column(name = "message")
    private String message;
}
