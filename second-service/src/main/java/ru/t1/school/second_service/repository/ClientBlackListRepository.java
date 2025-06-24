package ru.t1.school.second_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.school.second_service.model.ClientBlackList;

import java.util.Optional;
import java.util.UUID;

public interface ClientBlackListRepository extends JpaRepository<ClientBlackList, Long> {
    Optional<ClientBlackList> findByClientId(UUID clientId);
}