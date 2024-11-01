package com.onlineshop.payment_service.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.onlineshop.payment_service.model.Transaction;

@Repository
public class TransactionRepository {
    private final Map<Long, Transaction> transactionData = new HashMap<>();
    private Long idCounter = 1L;

    public Transaction save(Transaction transaction) {
        transaction.setId(idCounter++);
        transactionData.put(transaction.getId(), transaction);
        return transaction;
    }

    public Transaction findById(Long id) {
        return transactionData.get(id);
    }
}
