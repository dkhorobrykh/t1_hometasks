package ru.t1.school.main_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getAccounts() {
        var result = accountService.getAll();
        return accountMapper.toDto(result);
    }

    @GetMapping("{accountId}")
    @Operation(summary = "Get account by ID")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getAccountById(@PathVariable(name = "accountId") Long accountId) {
        var result = accountService.getById(accountId);
        return accountMapper.toDto(result);
    }

    @PostMapping
    @Operation(summary = "Create a new account")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@RequestBody AddAccountDto dto) {
        var result = accountService.createAccount(dto);
        return accountMapper.toDto(result);
    }

    @DeleteMapping("{accountId}")
    @Operation(summary = "Delete account by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable(name = "accountId") Long accountId) {
        accountService.deleteAccount(accountId);
    }

    @PutMapping("{accountId}")
    @Operation(summary = "Update account by ID")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto updateAccount(@PathVariable(name = "accountId") Long accountId, @RequestBody AddAccountDto dto) {
        var result = accountService.updateAccount(accountId, dto);
        return accountMapper.toDto(result);
    }
}
