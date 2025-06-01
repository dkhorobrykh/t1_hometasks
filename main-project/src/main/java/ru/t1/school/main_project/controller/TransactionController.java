package ru.t1.school.main_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.t1.school.main_project.model.dto.AddTransactionDto;
import ru.t1.school.main_project.model.dto.TransactionDto;
import ru.t1.school.main_project.model.mapper.TransactionMapper;
import ru.t1.school.main_project.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("transaction")
public class TransactionController {

    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Get all transactions")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionDto> getAllTransactions() {
        var result = transactionService.getAll();
        return transactionMapper.toDto(result);
    }

    @GetMapping("{transactionId}")
    @Operation(summary = "Get transaction by ID")
    @ResponseStatus(HttpStatus.OK)
    public TransactionDto getTransactionById(@PathVariable(name = "transactionId") Long transactionId) {
        var result = transactionService.getById(transactionId);
        return transactionMapper.toDto(result);
    }

    @PostMapping
    @Operation(summary = "Create a new transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto createTransaction(@RequestBody AddTransactionDto dto) {
        var result = transactionService.createTransaction(dto);
        return transactionMapper.toDto(result);
    }

    @DeleteMapping("{transactionId}")
    @Operation(summary = "Delete transaction by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable(name = "transactionId") Long transactionId) {
        transactionService.deleteTransaction(transactionId);
    }

    @PutMapping("{transactionId}")
    @Operation(summary = "Update transaction by ID")
    @ResponseStatus(HttpStatus.OK)
    public TransactionDto updateTransaction(@PathVariable(name = "transactionId") Long transactionId, @RequestBody AddTransactionDto dto) {
        var result = transactionService.updateTransaction(transactionId, dto);
        return transactionMapper.toDto(result);
    }
}
