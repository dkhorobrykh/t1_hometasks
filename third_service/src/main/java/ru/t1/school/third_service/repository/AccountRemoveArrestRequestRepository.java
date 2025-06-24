package ru.t1.school.third_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.school.third_service.model.AccountRemoveArrestRequest;

public interface AccountRemoveArrestRequestRepository extends JpaRepository<AccountRemoveArrestRequest, Long> {
}