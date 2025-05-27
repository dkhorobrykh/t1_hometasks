package ru.t1.school.main_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        var result = transactionService.getAll();
        return ResponseEntity.ok(transactionMapper.toDto(result));
    }

    @GetMapping("{transactionId}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable(name = "transactionId") Long transactionId) {
        var result = transactionService.getById(transactionId);
        return ResponseEntity.ok(transactionMapper.toDto(result));
    }

    @PostMapping
    @Operation(summary = "Create a new transaction")
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody AddTransactionDto dto) {
        var result = transactionService.createTransaction(dto);
        return ResponseEntity.ok(transactionMapper.toDto(result));
    }

    @DeleteMapping("{transactionId}")
    @Operation(summary = "Delete transaction by ID")
    public ResponseEntity<?> deleteTransaction(@PathVariable(name = "transactionId") Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{transactionId}")
    @Operation(summary = "Update transaction by ID")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable(name = "transactionId") Long transactionId, @RequestBody AddTransactionDto dto) {
        var result = transactionService.updateTransaction(transactionId, dto);
        return ResponseEntity.ok(transactionMapper.toDto(result));
    }
}
