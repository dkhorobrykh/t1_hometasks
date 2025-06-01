package ru.t1.school.main_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.main_project.aop.annotation.LogDataSourceError;
import ru.t1.school.main_project.exception.type.ClientNotFoundException;
import ru.t1.school.main_project.model.Client;
import ru.t1.school.main_project.model.dto.AddClientDto;
import ru.t1.school.main_project.repository.ClientRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

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
}
