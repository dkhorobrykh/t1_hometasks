package ru.t1.school.third_service.model;

import jakarta.persistence.*;
import lombok.*;
import ru.t1.school.common.model.ClientStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "client_unblock_request",
        schema = "public"
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClientUnblockRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(name = "client_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus;

    @Column(name = "timestamp", nullable = false)
    @Builder.Default
    private Instant timestamp = Instant.now();

    @Column(name = "result")
    private Boolean result;

    @Column(name = "message")
    private String message;

}
