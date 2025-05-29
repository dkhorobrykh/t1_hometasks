package ru.t1.school.main_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.t1.school.main_project.aop.annotation.Cached;
import ru.t1.school.main_project.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Cached("client")
    @Override
    @NonNull Optional<Client> findById(@NonNull Long id);
}