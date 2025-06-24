package ru.t1.school.main_project.external;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.t1.school.common.model.dto.AccountUnblockRequestDto;
import ru.t1.school.common.model.dto.AccountUnblockResponseDto;
import ru.t1.school.common.model.dto.ClientUnblockRequestDto;
import ru.t1.school.common.model.dto.ClientUnblockResponseDto;
import ru.t1.school.main_project.config.FeignConfig;

import java.util.List;

@FeignClient(
        value = "external-service-block",
        url = "${service-3.url}",
        configuration = {FeignConfig.class}
)
public interface BlockServiceClient {

    @PostMapping("/client/unblock")
    ResponseEntity<List<ClientUnblockResponseDto>> unblockClient(@RequestBody @Valid List<ClientUnblockRequestDto> dto);

    @PostMapping("/account/remove-arrest")
    ResponseEntity<List<AccountUnblockResponseDto>> removeArrestFromAccount(@RequestBody @Valid List<AccountUnblockRequestDto> dto);
}
