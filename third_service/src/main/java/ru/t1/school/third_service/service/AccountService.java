package ru.t1.school.third_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.school.common.model.AccountStatus;
import ru.t1.school.common.model.dto.AccountUnblockRequestDto;
import ru.t1.school.common.model.dto.AccountUnblockResponseDto;
import ru.t1.school.third_service.model.AccountRemoveArrestRequest;
import ru.t1.school.third_service.repository.AccountRemoveArrestRequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRemoveArrestRequestRepository accountRemoveArrestRequestRepository;

    public List<AccountUnblockResponseDto> removeArrest(List<AccountUnblockRequestDto> dto) {
        log.info("Получен запрос на снятие ареста со счетов {}", dto.stream().map(AccountUnblockRequestDto::getAccountId).toList());

        var response = new ArrayList<AccountUnblockResponseDto>();

        for (var req : dto) {
            var request = AccountRemoveArrestRequest.builder()
                    .accountId(req.getAccountId())
                    .accountStatus(req.getAccountStatus())
                    .build();

            boolean result;
            String message;

            if (req.getAccountStatus() == null || !req.getAccountStatus().equals(AccountStatus.ARRESTED)) {
                log.warn("Счет {} не находится в статусе ARRESTED, снятие ареста невозможно", req.getAccountId());
                result = false;
                message = "Счет не находится в статусе ARRESTED";
            } else {
                if (new Random().nextBoolean()) {
                    log.info("Арест со счета {} успешно снят", req.getAccountId());
                    result = true;
                    message = "Счет успешно разблокирован";
                } else {
                    log.info("Арест со счета {} не снят", req.getAccountId());
                    result = false;
                    message = "Не повезло, увы 🙁";
                }
            }

            request.setResult(false);
            request.setMessage(message);
            accountRemoveArrestRequestRepository.saveAndFlush(request);
            response.add(new AccountUnblockResponseDto(req.getAccountId(), result, message));
        }

        return response;
    }
}
