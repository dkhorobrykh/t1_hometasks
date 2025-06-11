package ru.t1.school.the_best_starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.school.the_best_starter.aop.LogErrorAspect;
import ru.t1.school.the_best_starter.aop.MetricAspect;
import ru.t1.school.the_best_starter.service.MetricsService;

@Configuration
public class AspectConfig {
    @Bean
    public LogErrorAspect logErrorAspect(MetricsService metricsService) {
        return new LogErrorAspect(metricsService);
    }

    @Bean
    public MetricAspect metricAspect(MetricsService metricsService) {
        return new MetricAspect(metricsService);
    }
}
