package ru.t1.school.main_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.main_project.model.TimeLimitExceedLog;
import ru.t1.school.main_project.repository.TimeLimitExceedLogRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeLimitExceedLogService {
    private final TimeLimitExceedLogRepository timeLimitExceedLogRepository;

    public void save(TimeLimitExceedLog logEntry) {
        timeLimitExceedLogRepository.save(logEntry);
    }
}
