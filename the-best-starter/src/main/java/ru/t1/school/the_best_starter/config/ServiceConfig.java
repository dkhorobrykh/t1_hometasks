package ru.t1.school.the_best_starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.school.the_best_starter.kafka.MetricsProducer;
import ru.t1.school.the_best_starter.repository.DataSourceErrorLogRepository;
import ru.t1.school.the_best_starter.repository.TimeLimitExceedLogRepository;
import ru.t1.school.the_best_starter.service.DataSourceErrorLogService;
import ru.t1.school.the_best_starter.service.MetricsService;
import ru.t1.school.the_best_starter.service.TimeLimitExceedLogService;

@Configuration
public class ServiceConfig {
    @Bean
    public DataSourceErrorLogService dataSourceErrorLogService(DataSourceErrorLogRepository dataSourceErrorLogRepository) {
        return new DataSourceErrorLogService(dataSourceErrorLogRepository);
    }

    @Bean
    public MetricsService metricsService(MetricsProducer producer, DataSourceErrorLogService dataSourceErrorLogService, TimeLimitExceedLogService timeLimitExceedLogService) {
        return new MetricsService(producer, dataSourceErrorLogService, timeLimitExceedLogService);
    }

    @Bean
    public TimeLimitExceedLogService timeLimitExceedLogService(TimeLimitExceedLogRepository timeLimitExceedLogRepository) {
        return new TimeLimitExceedLogService(timeLimitExceedLogRepository);
    }
}
