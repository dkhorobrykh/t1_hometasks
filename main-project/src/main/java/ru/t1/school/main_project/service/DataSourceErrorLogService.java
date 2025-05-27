package ru.t1.school.main_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.main_project.model.DataSourceErrorLog;
import ru.t1.school.main_project.repository.DataSourceErrorLogRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataSourceErrorLogService {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    public DataSourceErrorLog save(DataSourceErrorLog dataSourceErrorLog) {
        return dataSourceErrorLogRepository.saveAndFlush(dataSourceErrorLog);
    }
}
