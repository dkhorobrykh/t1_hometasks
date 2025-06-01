package ru.t1.school.main_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.school.main_project.aop.annotation.Metric;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {

    @PostMapping
    @Operation(summary = "Протестировать @Metric аспект")
    @ResponseStatus(HttpStatus.OK)
    @Metric
    public void test() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2500));
        } catch (InterruptedException ignored) {
        }
    }
}
