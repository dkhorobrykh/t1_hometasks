package ru.t1.school.the_best_starter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.t1.school.the_best_starter.repository")
public class JpaConfig {
}
