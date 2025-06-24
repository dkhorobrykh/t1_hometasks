package ru.t1.school.main_project.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.t1.school.common.model.AccountStatus;
import ru.t1.school.common.model.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "account",
        schema = "public"
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "accountWithClient",
                attributeNodes = {
                        @NamedAttributeNode(value = "client")
                }
        )
})
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private AccountType accountType;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Builder.Default
    private AccountStatus status = AccountStatus.OPEN;

    @Builder.Default
    @Column(name = "account_id", nullable = false, unique = true)
    private UUID accountId = UUID.randomUUID();

    @Column(name = "frozen_amount", nullable = false)
    @Builder.Default
    private BigDecimal frozenAmount = BigDecimal.ZERO;
}
