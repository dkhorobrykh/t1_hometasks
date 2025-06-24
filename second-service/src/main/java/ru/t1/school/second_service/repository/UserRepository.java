package ru.t1.school.second_service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.t1.school.second_service.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "roles")
    @Query("""
            SELECT user
            FROM User user
            WHERE user.id = :id""")
    Optional<User> findByIdWithRoles(Long id);
}