package ru.t1.school.main_project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.t1.school.common.model.AccountStatus;
import ru.t1.school.main_project.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Override
    @NonNull
    Optional<Account> findById(@NonNull Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "accountWithClient")
    Optional<Account> findByAccountId(UUID accountId);

    @Query("""
            SELECT account
            FROM Account account
            WHERE account.status = :arrestStatus""")
    List<Account> findArrested(AccountStatus arrestStatus, Pageable pageable);

    long countByStatus(AccountStatus status);
}