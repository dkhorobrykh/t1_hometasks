package ru.t1.school.main_project.metric;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.school.common.model.AccountStatus;
import ru.t1.school.common.model.ClientStatus;
import ru.t1.school.main_project.service.AccountService;
import ru.t1.school.main_project.service.ClientService;

@Component
@RequiredArgsConstructor
public class StatusMetrics {
    private final ClientService clientService;
    private final AccountService accountService;
    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        Gauge.builder("blocked_clients", this, StatusMetrics::getBlockedClients)
                .description("Количество заблокированных клиентов")
                .tag("status", ClientStatus.BLOCKED.name())
                .register(meterRegistry);

        Gauge.builder("arrested_accounts", this, StatusMetrics::getArrestedAccounts)
                .description("Количество арестованных счетов")
                .tag("status", AccountStatus.ARRESTED.name())
                .register(meterRegistry);
    }

    public long getBlockedClients() {
        return clientService.countBlockedClients();
    }

    public long getArrestedAccounts() {
        return accountService.countArrestedAccounts();
    }
}
