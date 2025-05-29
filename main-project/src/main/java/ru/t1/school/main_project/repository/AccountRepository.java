package ru.t1.school.main_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.t1.school.main_project.aop.annotation.Cached;
import ru.t1.school.main_project.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Cached("account")
    @Override
    @NonNull
    Optional<Account> findById(@NonNull Long id);
}