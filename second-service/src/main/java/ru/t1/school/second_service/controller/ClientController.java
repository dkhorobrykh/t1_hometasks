package ru.t1.school.second_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.t1.school.common.model.dto.ClientStatusResponse;
import ru.t1.school.second_service.service.ClientService;

import java.util.UUID;

@RestController
@RequestMapping("client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ClientStatusResponse getClientStatus(@PathVariable("clientId") UUID clientId) {
        return clientService.getClientStatus(clientId);
    }
}
