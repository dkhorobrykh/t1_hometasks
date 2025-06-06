package ru.t1.school.second_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.t1.school.common.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "transaction_history",
        schema = "public"
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;

    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private TransactionStatus status;
}