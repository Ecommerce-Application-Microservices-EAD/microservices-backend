package com.onlineshop.payment_service.exception;

public class TransactionFailureException extends RuntimeException {
    public TransactionFailureException(String message) {
        super(message);
    }
}