package ru.t1.school.main_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.t1.school.main_project.model.dto.AddClientDto;
import ru.t1.school.main_project.model.dto.ClientDto;
import ru.t1.school.main_project.model.mapper.ClientMapper;
import ru.t1.school.main_project.service.ClientService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("client")
public class ClientController {
    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @GetMapping
    @Operation(summary = "Get all clients")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientDto> getClients() {
        var result = clientService.getAll();
        return clientMapper.toDto(result);
    }

    @GetMapping("{clientId}")
    @Operation(summary = "Get client by ID")
    @ResponseStatus(HttpStatus.OK)
    public ClientDto getClientById(@PathVariable(name = "clientId") Long clientId) {
        var result = clientService.getById(clientId);
        return clientMapper.toDto(result);
    }

    @PostMapping
    @Operation(summary = "Create a new client")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto createClient(@RequestBody AddClientDto dto) {
        var result = clientService.createClient(dto);
        return clientMapper.toDto(result);
    }

    @DeleteMapping("{clientId}")
    @Operation(summary = "Delete client by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable(name = "clientId") Long clientId) {
        clientService.deleteClient(clientId);
    }

    @PutMapping("{clientId}")
    @Operation(summary = "Update client by ID")
    @ResponseStatus(HttpStatus.OK)
    public ClientDto updateClient(@PathVariable(name = "clientId") Long clientId, @RequestBody AddClientDto dto) {
        var result = clientService.updateClient(clientId, dto);
        return clientMapper.toDto(result);
    }
}
