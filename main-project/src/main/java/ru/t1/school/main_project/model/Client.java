package ru.t1.school.main_project.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.t1.school.common.model.ClientStatus;

import java.util.UUID;

@Entity
@Table(
        name = "client",
        schema = "public"
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Builder.Default
    @Column(name = "client_id", nullable = false, unique = true)
    private UUID clientId = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private ClientStatus status;
}
