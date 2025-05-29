package ru.t1.school.main_project.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.school.main_project.model.TimeLimitExceedLog;
import ru.t1.school.main_project.service.TimeLimitExceedLogService;

import java.time.Instant;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {

    private final TimeLimitExceedLogService timeLimitExceedLogService;
    @Value("${metric.maximumMethodDurationInMillis:1000}")
    private Long maximumMethodDuration;

    @Around("@annotation(ru.t1.school.main_project.aop.annotation.Metric)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        var start = System.currentTimeMillis();
        var signature = joinPoint.getSignature().toLongString();

        Object result;

        try {
            result = joinPoint.proceed();
        } finally {
            var end = System.currentTimeMillis();
            var duration = end - start;

            if (duration > maximumMethodDuration) {
                log.warn("Метод {} занял больше времени, чем ожидалось: {} мс (максимум: {} мс)", signature, duration, maximumMethodDuration);

                var logEntry = TimeLimitExceedLog.builder()
                        .startDatetime(Instant.ofEpochMilli(start))
                        .endDatetime(Instant.ofEpochMilli(end))
                        .signature(signature)
                        .duration(duration)
                        .build();
                timeLimitExceedLogService.save(logEntry);
            } else {
                log.info("Метод {} выполнен успешно за {} мс", signature, duration);
            }
        }

        return result;
    }
}
