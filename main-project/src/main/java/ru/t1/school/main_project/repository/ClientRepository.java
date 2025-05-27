package ru.t1.school.main_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.school.main_project.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}