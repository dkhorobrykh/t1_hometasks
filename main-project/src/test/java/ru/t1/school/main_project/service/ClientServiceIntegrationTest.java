package ru.t1.school.main_project.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.t1.school.common.model.ClientStatus;
import ru.t1.school.main_project.BaseIntegrationTest;
import ru.t1.school.main_project.model.Client;
import ru.t1.school.main_project.repository.ClientRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientServiceIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Test
    void unblockClients_WithOnlyBlockedClients_ShouldUpdateAllStatuses() {
        // given
        List<Client> clients = List.of(
                Client.builder()
                        .clientId(UUID.fromString("b3bf1875-197a-46fe-b4a4-089de437df57"))
                        .firstName("John")
                        .lastName("Doe")
                        .middleName(null)
                        .status(ClientStatus.BLOCKED)
                        .build(),
                Client.builder()
                        .clientId(UUID.fromString("555e6a58-9f75-4b09-b68f-47510c1310ed"))
                        .firstName("Jane")
                        .lastName("Smith")
                        .middleName("A.")
                        .status(ClientStatus.BLOCKED)
                        .build(),
                Client.builder()
                        .clientId(UUID.fromString("97518d19-905c-4c6a-ba82-25d93fc33f95"))
                        .firstName("Alice")
                        .lastName("Johnson")
                        .middleName("B.")
                        .status(ClientStatus.BLOCKED)
                        .build()
        );

        clientRepository.deleteAll();
        clientRepository.saveAllAndFlush(clients);

        // when
        clientService.unblockClients();
        var clientsAfterUnblock = clientRepository.findAll();

        // then
        assertThat(clientsAfterUnblock).hasSize(3);
        assertThat(clientsAfterUnblock.get(0).getStatus()).isEqualTo(ClientStatus.ACTIVE);
        assertThat(clientsAfterUnblock.get(1).getStatus()).isEqualTo(ClientStatus.ACTIVE);
        assertThat(clientsAfterUnblock.get(2).getStatus()).isEqualTo(ClientStatus.ACTIVE);
    }

    @Test
    void unblockClients_WithDifferentClients_ShouldUpdateOnlyBlockedStatuses() {
        // given
        List<Client> clients = List.of(
                Client.builder()
                        .clientId(UUID.fromString("b3bf1875-197a-46fe-b4a4-089de437df57"))
                        .firstName("John")
                        .lastName("Doe")
                        .middleName(null)
                        .status(null)
                        .build(),
                Client.builder()
                        .clientId(UUID.fromString("555e6a58-9f75-4b09-b68f-47510c1310ed"))
                        .firstName("Jane")
                        .lastName("Smith")
                        .middleName("A.")
                        .status(ClientStatus.ACTIVE)
                        .build(),
                Client.builder()
                        .clientId(UUID.fromString("97518d19-905c-4c6a-ba82-25d93fc33f95"))
                        .firstName("Alice")
                        .lastName("Johnson")
                        .middleName("B.")
                        .status(ClientStatus.BLOCKED)
                        .build()
        );

        clientRepository.deleteAll();
        clientRepository.saveAllAndFlush(clients);

        // when
        clientService.unblockClients();
        var firstClient = clientRepository.findByClientId(UUID.fromString("b3bf1875-197a-46fe-b4a4-089de437df57")).orElseThrow();
        var secondClient = clientRepository.findByClientId(UUID.fromString("555e6a58-9f75-4b09-b68f-47510c1310ed")).orElseThrow();
        var thirdClient = clientRepository.findByClientId(UUID.fromString("97518d19-905c-4c6a-ba82-25d93fc33f95")).orElseThrow();

        // then
        assertThat(firstClient.getStatus()).isEqualTo(null);
        assertThat(secondClient.getStatus()).isEqualTo(ClientStatus.ACTIVE);
        assertThat(thirdClient.getStatus()).isEqualTo(ClientStatus.ACTIVE);
    }

    @Test
    void updateClientStatus_WithCorrectData_ShouldUpdateClientStatus() {
        // given
        UUID clientId = UUID.fromString("b3bf1875-197a-46fe-b4a4-089de437df69");
        Client client = Client.builder()
                .clientId(clientId)
                .firstName("John")
                .lastName("Doe")
                .middleName(null)
                .status(null)
                .build();

        clientRepository.saveAndFlush(client);

        // when
        Client updatedClient = clientService.updateClientStatus(clientId);

        // then
        assertThat(updatedClient.getStatus()).isEqualTo(ClientStatus.ACTIVE);
    }
}
