// src/main/java/com/example/paymentservice/repository/PaymentRepository.java

package com.onlineshop.payment_service.repository;

import com.onlineshop.payment_service.model.Payment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
