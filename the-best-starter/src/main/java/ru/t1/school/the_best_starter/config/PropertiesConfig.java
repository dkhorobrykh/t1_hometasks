package ru.t1.school.the_best_starter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StarterProperties.class)
public class PropertiesConfig {
}
