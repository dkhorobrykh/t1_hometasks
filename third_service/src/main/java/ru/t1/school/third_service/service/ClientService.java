package ru.t1.school.third_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.common.model.ClientStatus;
import ru.t1.school.common.model.dto.ClientUnblockRequestDto;
import ru.t1.school.common.model.dto.ClientUnblockResponseDto;
import ru.t1.school.third_service.model.ClientUnblockRequest;
import ru.t1.school.third_service.repository.ClientUnblockRequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    private final ClientUnblockRequestRepository clientUnblockRequestRepository;

    public List<ClientUnblockResponseDto> unblockClient(List<ClientUnblockRequestDto> dto) {
        log.info("Получен запрос на разблокировку клиентов {}", dto.stream().map(ClientUnblockRequestDto::getClientId).toList());

        var response = new ArrayList<ClientUnblockResponseDto>();

        for (var req : dto) {
            var request = ClientUnblockRequest.builder().clientId(req.getClientId()).clientStatus(req.getClientStatus()).build();

            boolean result;
            String message;

            if (req.getClientStatus() == null || !req.getClientStatus().equals(ClientStatus.BLOCKED)) {
                log.warn("Клиент {} не находится в статусе BLOCKED, разблокировка не удалась", req.getClientId());
                result = false;
                message = "Клиент не находится в статусе BLOCKED";
            } else {
                if (new Random().nextBoolean()) {
                    log.info("Клиент {} успешно разблокирован", req.getClientId());
                    result = true;
                    message = "Клиент успешно разблокирован";
                } else {
                    log.info("Клиент {} не разблокирован", req.getClientId());
                    result = false;
                    message = "Не повезло, увы 🙁";
                }
            }

            request.setResult(result);
            request.setMessage(message);
            clientUnblockRequestRepository.saveAndFlush(request);
            response.add(new ClientUnblockResponseDto(req.getClientId(), result, message));
        }

        return response;
    }
}
