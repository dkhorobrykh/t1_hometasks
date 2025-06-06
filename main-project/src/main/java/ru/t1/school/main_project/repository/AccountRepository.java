package ru.t1.school.main_project.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.t1.school.main_project.aop.annotation.Cached;
import ru.t1.school.main_project.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Override
    @NonNull
    Optional<Account> findById(@NonNull Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "accountWithClient")
    Optional<Account> findByAccountId(UUID accountId);
}