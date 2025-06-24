package ru.t1.school.third_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.t1.school.common.model.dto.ClientUnblockRequestDto;
import ru.t1.school.common.model.dto.ClientUnblockResponseDto;
import ru.t1.school.third_service.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("unblock")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientUnblockResponseDto> unblockClient(@RequestBody @Valid List<ClientUnblockRequestDto> dto) {
        return clientService.unblockClient(dto);
    }
}
