package ru.t1.school.second_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.common.model.ClientStatus;
import ru.t1.school.common.model.dto.ClientStatusResponse;
import ru.t1.school.second_service.repository.ClientBlackListRepository;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    private final ClientBlackListRepository clientBlackListRepository;

    public ClientStatusResponse getClientStatus(UUID clientId) {
        if (clientBlackListRepository.findByClientId(clientId).isPresent()) {
            log.info("Клиент {} находится в черном списке", clientId);
            return new ClientStatusResponse(clientId, ClientStatus.BLOCKED);
        } else {
            log.info("Клиент {} не находится в черном списке", clientId);
            return new ClientStatusResponse(clientId, ClientStatus.ACTIVE);
        }
    }
}
