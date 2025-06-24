package ru.t1.school.main_project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.t1.school.common.model.ClientStatus;
import ru.t1.school.main_project.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Override
    @NonNull
    Optional<Client> findById(@NonNull Long id);

    Optional<Client> findByClientId(UUID clientId);

    @Query("""
            SELECT client
            FROM Client client
            WHERE client.status = :blockedStatus""")
    List<Client> findBlocked(ClientStatus blockedStatus, Pageable pageable);

    long countByStatus(ClientStatus status);
}