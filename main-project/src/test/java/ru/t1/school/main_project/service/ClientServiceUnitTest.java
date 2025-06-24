package ru.t1.school.main_project.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.t1.school.common.model.ClientStatus;
import ru.t1.school.main_project.BaseUnitTest;
import ru.t1.school.main_project.model.Client;
import ru.t1.school.main_project.model.dto.AddClientDto;
import ru.t1.school.main_project.repository.ClientRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ClientServiceUnitTest extends BaseUnitTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"middleName"})
    void createClient_WithFullName_shouldPersistCorrectEntity(String middleName) {
        // given
        var dto = AddClientDto.builder()
                .firstName("firstName")
                .middleName(middleName)
                .lastName("lastName")
                .build();

        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));

        // when
        var result = clientService.createClient(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(result.getMiddleName()).isEqualTo(dto.getMiddleName());
        assertThat(result.getLastName()).isEqualTo(dto.getLastName());
    }
}
