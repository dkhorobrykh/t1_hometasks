package ru.t1.school.the_best_starter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.t1.school.the_best_starter.model.DataSourceErrorLog;
import ru.t1.school.the_best_starter.repository.DataSourceErrorLogRepository;

@Slf4j
@RequiredArgsConstructor
public class DataSourceErrorLogService {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    public DataSourceErrorLog save(DataSourceErrorLog dataSourceErrorLog) {
        return dataSourceErrorLogRepository.saveAndFlush(dataSourceErrorLog);
    }
}
