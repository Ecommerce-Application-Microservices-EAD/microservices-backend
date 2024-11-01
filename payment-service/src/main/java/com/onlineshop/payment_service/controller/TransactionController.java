package com.onlineshop.payment_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshop.payment_service.dto.TransactionRequestDTO;
import com.onlineshop.payment_service.dto.TransactionResponseDTO;
import com.onlineshop.payment_service.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/initiate")
    public TransactionResponseDTO initiateTransaction(@RequestBody TransactionRequestDTO requestDto) {
        return transactionService.initiateTransaction(requestDto);
    }

    @PostMapping("/confirm/{id}")
    public TransactionResponseDTO confirmTransaction(@PathVariable Long id) {
        return transactionService.confirmTransaction(id);
    }
}
