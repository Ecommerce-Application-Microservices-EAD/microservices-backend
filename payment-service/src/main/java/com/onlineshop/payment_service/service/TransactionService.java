package com.onlineshop.payment_service.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlineshop.payment_service.dto.TransactionRequestDTO;
import com.onlineshop.payment_service.dto.TransactionResponseDTO;
import com.onlineshop.payment_service.exception.ResourceNotFoundException;
import com.onlineshop.payment_service.exception.TransactionFailureException;
import com.onlineshop.payment_service.model.Transaction;
import com.onlineshop.payment_service.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionResponseDTO initiateTransaction(TransactionRequestDTO requestDto) {
        Transaction transaction = new Transaction();
        transaction.setPaymentId(requestDto.getPaymentId());
        transaction.setAmount(requestDto.getAmount());
        transaction.setStatus("PENDING");
        transaction.setTimestamp(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponseDto(savedTransaction);
    }

    public TransactionResponseDTO confirmTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId);

        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found with id: " + transactionId);
        }

        if (!"PENDING".equals(transaction.getStatus())) {
            throw new TransactionFailureException(
                    "Transaction with id: " + transactionId + " is not in a confirmable state.");
        }

        if (transaction != null && "PENDING".equals(transaction.getStatus())) {
            transaction.setStatus("COMPLETED");
            transactionRepository.save(transaction);
        }
        return mapToResponseDto(transaction);
    }

    private TransactionResponseDTO mapToResponseDto(Transaction transaction) {
        TransactionResponseDTO responseDto = new TransactionResponseDTO();
        responseDto.setId(transaction.getId());
        responseDto.setPaymentId(transaction.getPaymentId());
        responseDto.setAmount(transaction.getAmount());
        responseDto.setStatus(transaction.getStatus());
        responseDto.setTimestamp(transaction.getTimestamp());
        return responseDto;
    }

    public TransactionResponseDTO getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId);
        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found.");
        }
        return mapToResponseDto(transaction);
    }
}