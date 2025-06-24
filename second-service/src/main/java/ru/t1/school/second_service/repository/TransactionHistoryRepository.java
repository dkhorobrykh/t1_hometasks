package ru.t1.school.second_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.t1.school.second_service.model.TransactionHistory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    @Query("""
            SELECT history
            FROM TransactionHistory history
            WHERE history.clientId = :clientId
                        AND history.accountId = :accountId
                        AND history.timestamp >= :minTimeForTimestamp
                        AND history.timestamp <= :maxTimeForTimestamp
            ORDER BY history.id DESC
            """)
    List<TransactionHistory> findAllLastTransactionsByClientAndAccount(
            @Param("clientId") UUID clientId,
            @Param("accountId") UUID accountId,
            @Param("minTimeForTimestamp") Instant minTimeForTimestamp,
            @Param("maxTimeForTimestamp") Instant maxTimeForTimestamp);
}