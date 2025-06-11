package ru.t1.school.the_best_starter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.t1.school.the_best_starter.model.TimeLimitExceedLog;
import ru.t1.school.the_best_starter.repository.TimeLimitExceedLogRepository;

@Slf4j
@RequiredArgsConstructor
public class TimeLimitExceedLogService {
    private final TimeLimitExceedLogRepository timeLimitExceedLogRepository;

    public void save(TimeLimitExceedLog logEntry) {
        timeLimitExceedLogRepository.save(logEntry);
    }
}
