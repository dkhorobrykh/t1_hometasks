package ru.t1.school.main_project.aop.cache;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CacheEntry {
    private Object object;
    private Instant expirationTime;
}
