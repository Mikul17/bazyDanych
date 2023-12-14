package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.TransactionRequest;
import com.mikul17.bazyDanych.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction (@RequestBody TransactionRequest transactionRequest) {
        try {
            return ResponseEntity.ok().body(transactionService.performTransaction(transactionRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransactions() {
        try {
            return ResponseEntity.ok().body(transactionService.getAllTransactions());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<?> getAllTransactionsByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok().body(transactionService.getAllTransactionsByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all/won/{userId}")
    public ResponseEntity<?> getAllWonTransactions(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok().body(transactionService.getTransactionByUserAndType(userId, "PAYOUT"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all/withdraw/{userId}")
    public ResponseEntity<?> getAllWithdrawTransactions(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok().body(transactionService.getTransactionByUserAndType(userId, "WITHDRAW"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all/deposit/{userId}")
    public ResponseEntity<?> getAllDepositTransactions(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok().body(transactionService.getTransactionByUserAndType(userId, "DEPOSIT"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
