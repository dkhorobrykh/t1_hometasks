package ru.t1.school.main_project.task;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.t1.school.main_project.service.ClientService;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class UnblockClientTask {

    private final ClientService clientService;

    @Scheduled(fixedDelayString = "${client.unblock.delay-in-seconds:60}", timeUnit = TimeUnit.SECONDS)
    public void unblockClients() {
        clientService.unblockClients();
    }
}
