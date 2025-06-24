package ru.t1.school.main_project.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.school.main_project.aop.annotation.Cached;
import ru.t1.school.main_project.aop.cache.CacheEntry;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CachedAspect {

    private static final ConcurrentMap<String, ConcurrentMap<Long, CacheEntry>> cache = new ConcurrentHashMap<>();
    @Value("${cache.defaultExpirationInSeconds:60}")
    private Long defaultExpirationInSeconds;

    @Around("@annotation(cached)")
    public Object around(ProceedingJoinPoint joinPoint, Cached cached) throws Throwable {
        var type = cached.value();
        var args = joinPoint.getArgs();
        if (args.length != 1) {
            throw new IllegalArgumentException("Метод с аннотацией @Cached должен принимать ровно один аргумент");
        }
        Long neededId;
        try {
            neededId = (Long) args[0];
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Метод с аннотацией @Cached должен принимать аргумент типа Long");
        }

        var cachedValue = getFromCache(type, neededId);

        if (cachedValue != null) {
            if (Instant.now().isAfter(cachedValue.getExpirationTime())) {
                log.info("Кэшированный объект {} с id {} устарел, удаляем старый и запрашиваем новый", type, neededId);
                removeFromCache(type, neededId);
            } else {
                log.info("Возвращаем кэшированный объект {} с id {}", type, neededId);
                return cachedValue.getObject();
            }
        } else {
            log.info("Кэшированный объект {} с id {} не найден, запрашиваем новый", type, neededId);
        }

        var result = joinPoint.proceed();

        if (!(result instanceof Optional && ((Optional<?>) result).isEmpty())) {
            var newCacheEntry = CacheEntry.builder()
                    .object(result)
                    .expirationTime(Instant.now().plus(defaultExpirationInSeconds, ChronoUnit.SECONDS))
                    .build();

            putInCache(type, neededId, newCacheEntry);
        }

        return result;
    }

    private CacheEntry getFromCache(String type, Long id) {
        return cache.getOrDefault(type, new ConcurrentHashMap<>()).getOrDefault(id, null);
    }

    private void putInCache(String type, Long id, CacheEntry entry) {
        cache.computeIfAbsent(type, _ -> new ConcurrentHashMap<>()).put(id, entry);
    }

    private void removeFromCache(String type, Long id) {
        var typeCache = cache.get(type);
        if (typeCache != null) {
            typeCache.remove(id);
        }
    }
}
