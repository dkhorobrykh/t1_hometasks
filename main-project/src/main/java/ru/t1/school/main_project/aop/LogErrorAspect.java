package ru.t1.school.main_project.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.t1.school.main_project.model.DataSourceErrorLog;
import ru.t1.school.main_project.service.DataSourceErrorLogService;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogErrorAspect {
    private final DataSourceErrorLogService dataSourceErrorLogService;

    @AfterThrowing(
            pointcut = "@annotation(ru.t1.school.main_project.aop.annotation.LogDataSourceError)",
            throwing = "ex"
    )
    public void logDataSourceError(JoinPoint joinPoint, Exception ex) {
        var stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
        var message = ex.getMessage();
        var signature = joinPoint.getSignature().toLongString();

        var log = DataSourceErrorLog.builder()
                .stackTrace(stackTrace)
                .message(message)
                .signature(signature)
                .build();

        dataSourceErrorLogService.save(log);
    }
}
