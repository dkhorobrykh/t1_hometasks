package ru.t1.school.main_project.config;

import feign.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableFeignClients(basePackages = {"ru.t1.school.main_project.external"})
public class FeignConfig {
    @Bean
    Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }
}
