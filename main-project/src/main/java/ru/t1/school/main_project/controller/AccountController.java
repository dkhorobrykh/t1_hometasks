package ru.t1.school.main_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.school.main_project.model.dto.AddAccountDto;
import ru.t1.school.main_project.model.dto.AccountDto;
import ru.t1.school.main_project.model.mapper.AccountMapper;
import ru.t1.school.main_project.service.AccountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("account")
public class AccountController {
    private final AccountMapper accountMapper;
    private final AccountService accountService;

    @GetMapping
    @Operation(summary = "Get all accounts")
    public ResponseEntity<List<AccountDto>> getAccounts() {
        var result = accountService.getAll();
        return ResponseEntity.ok(accountMapper.toDto(result));
    }

    @GetMapping("{accountId}")
    @Operation(summary = "Get account by ID")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(name = "accountId") Long accountId) {
        var result = accountService.getById(accountId);
        return ResponseEntity.ok(accountMapper.toDto(result));
    }

    @PostMapping
    @Operation(summary = "Create a new account")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AddAccountDto dto) {
        var result = accountService.createAccount(dto);
        return ResponseEntity.ok(accountMapper.toDto(result));
    }

    @DeleteMapping("{accountId}")
    @Operation(summary = "Delete account by ID")
    public ResponseEntity<?> deleteAccount(@PathVariable(name = "accountId") Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{accountId}")
    @Operation(summary = "Update account by ID")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable(name = "accountId") Long accountId, @RequestBody AddAccountDto dto) {
        var result = accountService.updateAccount(accountId, dto);
        return ResponseEntity.ok(accountMapper.toDto(result));
    }
}
