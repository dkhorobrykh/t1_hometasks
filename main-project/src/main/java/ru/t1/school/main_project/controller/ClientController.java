package ru.t1.school.main_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ClientDto>> getClients() {
        var result = clientService.getAll();
        return ResponseEntity.ok(clientMapper.toDto(result));
    }

    @GetMapping("{clientId}")
    @Operation(summary = "Get client by ID")
    public ResponseEntity<ClientDto> getClientById(@PathVariable(name = "clientId") Long clientId) {
        var result = clientService.getById(clientId);
        return ResponseEntity.ok(clientMapper.toDto(result));
    }

    @PostMapping
    @Operation(summary = "Create a new client")
    public ResponseEntity<ClientDto> createClient(@RequestBody AddClientDto dto) {
        var result = clientService.createClient(dto);
        return ResponseEntity.ok(clientMapper.toDto(result));
    }

    @DeleteMapping("{clientId}")
    @Operation(summary = "Delete client by ID")
    public ResponseEntity<?> deleteClient(@PathVariable(name = "clientId") Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{clientId}")
    @Operation(summary = "Update client by ID")
    public ResponseEntity<ClientDto> updateClient(@PathVariable(name = "clientId") Long clientId, @RequestBody AddClientDto dto) {
        var result = clientService.updateClient(clientId, dto);
        return ResponseEntity.ok(clientMapper.toDto(result));
    }
}
