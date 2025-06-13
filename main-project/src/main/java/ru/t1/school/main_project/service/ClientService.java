package ru.t1.school.main_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.t1.school.common.model.ClientStatus;
import ru.t1.school.common.model.dto.ClientUnblockRequestDto;
import ru.t1.school.main_project.exception.type.ClientNotFoundException;
import ru.t1.school.main_project.external.ExternalBlockService;
import ru.t1.school.main_project.external.ExternalClientService;
import ru.t1.school.main_project.model.Client;
import ru.t1.school.main_project.model.dto.AddClientDto;
import ru.t1.school.main_project.repository.ClientRepository;
import ru.t1.school.the_best_starter.aop.annotation.LogDataSourceError;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ExternalClientService externalClientService;
    private final ExternalBlockService externalBlockService;
    @Value("${client.unblock.quantity-per-time:5}")
    private int quantityPerTime;

    @LogDataSourceError
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @LogDataSourceError
    public Client getById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));
    }

    @LogDataSourceError
    public Client createClient(AddClientDto dto) {
        var client = Client.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .build();

        return clientRepository.save(client);
    }

    @LogDataSourceError
    public void deleteClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ClientNotFoundException(clientId);
        }
        clientRepository.deleteById(clientId);
    }

    @LogDataSourceError
    public Client updateClient(Long clientId, AddClientDto dto) {
        var client = getById(clientId);
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setMiddleName(dto.getMiddleName());
        return clientRepository.save(client);
    }

    public Client getByClientId(UUID clientId) {
        return clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));
    }

    public Client updateClientStatus(UUID clientId) {
        var result = externalClientService.getClientStatus(clientId);
        if (result == null) {
            log.error("Не удалось получить статус клиента с id {}", clientId);
            return null;
        } else {
            var client = getByClientId(clientId);
            client.setStatus(result.getStatus());
            return clientRepository.saveAndFlush(client);
        }
    }

    public void unblockClients() {
        log.debug("Начало разблокировки клиентов");

        var limit = PageRequest.of(0, quantityPerTime);
        var clients = clientRepository.findBlocked(ClientStatus.BLOCKED, limit);
        var unblockedClients = new ArrayList<Client>();

        try {
            var dto = clients.stream().map(client ->
                    ClientUnblockRequestDto.builder()
                            .clientId(client.getClientId())
                            .clientStatus(client.getStatus())
                            .build()).toList();

            if (dto.isEmpty()) {
                return;
            }

            var response = externalBlockService.unblockClient(dto);

            for (var res : response) {
                var client = getByClientId(res.getClientId());

                if (res.getResult()) {
                    client.setStatus(ClientStatus.ACTIVE);
                    clientRepository.saveAndFlush(client);
                    unblockedClients.add(getById(client.getId()));
                    log.info("Клиент {} успешно разблокирован", client.getClientId());
                } else {
                    log.warn("Не удалось разблокировать клиента {}: {}", client.getClientId(), res.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Ошибка при разблокировке клиентов: {}", e.getMessage());
        }

        log.debug("Окончание разблокировки клиентов. Количество разблокированных клиентов: {} / {}", unblockedClients.size(), clients.size());
    }

    public long countBlockedClients() {
        return clientRepository.countByStatus(ClientStatus.BLOCKED);
    }
}
