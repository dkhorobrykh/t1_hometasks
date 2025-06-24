package ru.t1.school.main_project.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.common.model.dto.AccountUnblockRequestDto;
import ru.t1.school.common.model.dto.AccountUnblockResponseDto;
import ru.t1.school.common.model.dto.ClientUnblockRequestDto;
import ru.t1.school.common.model.dto.ClientUnblockResponseDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalBlockService {
    private final BlockServiceClient blockServiceClient;

    public List<ClientUnblockResponseDto> unblockClient(List<ClientUnblockRequestDto> dto) {
        var response = blockServiceClient.unblockClient(dto);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Ошибка при разблокировке клиентов: {}", response.getStatusCode());
            throw new RuntimeException("Ошибка при разблокировке клиентов");
        }

        return response.getBody();
    }

    public List<AccountUnblockResponseDto> removeArrestFromAccount(List<AccountUnblockRequestDto> dto) {
        var response = blockServiceClient.removeArrestFromAccount(dto);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Ошибка при снятии ареста со счетов: {}", response.getStatusCode());
            throw new RuntimeException("Ошибка при снятии ареста со счетов");
        }

        return response.getBody();
    }
}
