package ru.t1.school.the_best_starter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.school.the_best_starter.model.DataSourceErrorLog;

public interface DataSourceErrorLogRepository extends JpaRepository<DataSourceErrorLog, Long> {
}