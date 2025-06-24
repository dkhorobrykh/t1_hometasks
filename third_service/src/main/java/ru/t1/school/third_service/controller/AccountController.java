package ru.t1.school.third_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.school.common.model.dto.AccountUnblockRequestDto;
import ru.t1.school.common.model.dto.AccountUnblockResponseDto;
import ru.t1.school.third_service.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("remove-arrest")
    public List<AccountUnblockResponseDto> removeArrest(@RequestBody @Valid List<AccountUnblockRequestDto> dto) {
        return accountService.removeArrest(dto);
    }
}
