package ru.t1.school.second_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "client_black_list",
        schema = "public"
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false, unique = true)
    private UUID clientId;
}
