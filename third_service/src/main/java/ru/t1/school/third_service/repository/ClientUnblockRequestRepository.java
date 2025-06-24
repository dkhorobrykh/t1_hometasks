package ru.t1.school.third_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.school.third_service.model.ClientUnblockRequest;

public interface ClientUnblockRequestRepository extends JpaRepository<ClientUnblockRequest, Long> {
}