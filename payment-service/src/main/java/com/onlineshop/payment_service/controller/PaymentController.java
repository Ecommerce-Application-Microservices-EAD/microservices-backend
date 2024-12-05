package com.onlineshop.payment_service.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshop.payment_service.exception.ResourceNotFoundException;
import com.onlineshop.payment_service.model.Payment;
import com.onlineshop.payment_service.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(@RequestBody Map<String, Object> paymentData) {
        try {
            Long amount = Long.valueOf(paymentData.get("amount").toString());
            String currency = paymentData.get("currency").toString();
            String userId = paymentData.get("userId").toString();

            String res = paymentService.createPayment(amount, currency, userId);
            String[] parts = res.split(", ");
            String clientSecret = parts[1];
            String paymentId = parts[0];

            return ResponseEntity.ok(Map.of("clientSecret", clientSecret, "paymentId", paymentId));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid payment details", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating payment", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable String id) {
        try {
            Payment payment = paymentService.getPayment(id);
            return ResponseEntity.ok(payment);
        } catch (ResourceNotFoundException e) {
            logger.error("Payment not found", e);
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable String id,
            @RequestBody Map<String, String> statusData) {
        try {
            String status = statusData.get("status");
            Payment updatedPayment = paymentService.updatePaymentStatus(id, status);
            return ResponseEntity.ok(updatedPayment);
        } catch (ResourceNotFoundException e) {
            logger.error("Payment not found", e);
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<String> confirmPayment(@PathVariable String id) {
        try {
            paymentService.confirmPayment(id);
            return ResponseEntity.ok("Payment confirmed");
        } catch (ResourceNotFoundException e) {
            logger.error("Payment not found", e);
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error confirming payment", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelPayment(@PathVariable String id) {
        try {
            paymentService.cancelPayment(id);
            return ResponseEntity.ok("Payment cancelled");
        } catch (ResourceNotFoundException e) {
            logger.error("Payment not found", e);
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error cancelling payment", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
