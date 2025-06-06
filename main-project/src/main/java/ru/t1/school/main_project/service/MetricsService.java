package ru.t1.school.main_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.main_project.kafka.MetricsProducer;
import ru.t1.school.common.kafka.dto.MetricsMessage;
import ru.t1.school.common.kafka.dto.MetricsType;
import ru.t1.school.main_project.model.DataSourceErrorLog;
import ru.t1.school.main_project.model.TimeLimitExceedLog;

@Service
@Slf4j
@RequiredArgsConstructor
public class MetricsService {
    private final MetricsProducer producer;
    private final DataSourceErrorLogService dataSourceErrorLogService;
    private final TimeLimitExceedLogService timeLimitExceedLogService;

    public void saveDataSourceError(DataSourceErrorLog logMsg) {
        if (!sendDataSourceError(logMsg)) {
            dataSourceErrorLogService.save(logMsg);
        }
    }

    public void saveTimeLimitExceedLog(TimeLimitExceedLog logMsg) {
        if (!sendTimeLimitExceedLog(logMsg)) {
            timeLimitExceedLogService.save(logMsg);
        }
    }

    private boolean sendDataSourceError(DataSourceErrorLog logMsg) {
        var message = MetricsMessage.builder()
                .signature(logMsg.getSignature())
                .stacktrace(logMsg.getStackTrace())
                .message(logMsg.getMessage())
                .build();
        return producer.send(message, MetricsType.DATA_SOURCE);
    }

    private boolean sendTimeLimitExceedLog(TimeLimitExceedLog logMsg) {
        var message = MetricsMessage.builder()
                .signature(logMsg.getSignature())
                .duration(logMsg.getDuration())
                .build();
        return producer.send(message, MetricsType.METRICS);
    }
}
