package ru.t1.school.main_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.t1.school.main_project.aop.annotation.Cached;
import ru.t1.school.main_project.model.Account;
import ru.t1.school.main_project.model.Transaction;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Override
    @NonNull
    Optional<Transaction> findById(@NonNull Long id);

    Optional<Transaction> findByTransactionId(UUID transactionId);
}