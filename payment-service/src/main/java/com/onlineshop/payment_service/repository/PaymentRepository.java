package com.onlineshop.payment_service.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.onlineshop.payment_service.model.Payment;

@Repository
public class PaymentRepository {
    private final Map<Long, Payment> paymentData = new HashMap<>();
    private Long idCounter = 1L;

    public Payment save(Payment payment) {
        payment.setId(idCounter++);
        paymentData.put(payment.getId(), payment);
        return payment;
    }

    public Payment findById(Long id) {
        return paymentData.get(id);
    }

    public void deleteById(Long id) {
        paymentData.remove(id);
    }

    public void deleteAll() {
        paymentData.clear();
    }

    public Map<Long, Payment> getPaymentData() {
        System.out.println("Get - /api/payments/all - repository called");
        System.out.println("Payment data: " + paymentData);
        return paymentData;
    }

    
}
