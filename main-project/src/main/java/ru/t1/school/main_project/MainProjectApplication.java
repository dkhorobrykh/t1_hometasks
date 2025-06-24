package ru.t1.school.main_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan({
        "ru.t1.school.the_best_starter.model",
        "ru.t1.school.main_project"
})
public class MainProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainProjectApplication.class, args);
    }

}
