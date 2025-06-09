package ru.t1.school.main_project.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

public class FeignRequestInterceptor implements RequestInterceptor {
    @Value("${service-2.token}")
    private String token;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token);
    }
}
