package ru.t1.school.main_project.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.t1.school.main_project.service.AccountService;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class RemoveArrestFromAccountsTask {

    private final AccountService accountService;

    @Scheduled(fixedDelayString = "${account.remove-arrest.delay-in-seconds:60}", timeUnit = TimeUnit.SECONDS)
    public void removeArrestFromAccounts() {
        accountService.removeArrestFromAccounts();
    }
}
