package ru.t1.school.main_project.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.common.model.dto.ClientStatusResponse;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalClientService {
    private final ClientServiceClient clientServiceClient;

    public ClientStatusResponse getClientStatus(UUID clientId) {
        var response = clientServiceClient.getClientStatus(clientId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Ошибка при получении статуса клиента {}: {}", clientId, response.getStatusCode());
            throw new RuntimeException("Ошибка при получении статуса клиента");
        }

        return response.getBody();
    }
}
