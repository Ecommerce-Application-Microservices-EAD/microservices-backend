package com.onlineshop.payment_service.controller;

import com.onlineshop.payment_service.model.Payment;
import com.onlineshop.payment_service.service.PaymentService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/payments")
// @CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    /**
     * Creates a payment.
     *
     * @param paymentData the payment data
     * @return the response entity with client secret and payment ID
     */
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
        } catch (Exception e) {
            logger.error("Error creating payment", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Retrieves a payment by ID.
     *
     * @param id the payment ID
     * @return the response entity with the payment
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable String id) {
        Payment payment = paymentService.getPayment(id);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates the status of a payment.
     *
     * @param id the payment ID
     * @param statusData the status data
     * @return the response entity with the updated payment
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable String id,
            @RequestBody Map<String, String> statusData) {
        String status = statusData.get("status");
        Payment updatedPayment = paymentService.updatePaymentStatus(id, status);
        if (updatedPayment != null) {
            return ResponseEntity.ok(updatedPayment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Confirms a payment.
     *
     * @param id the payment ID
     * @return the response entity with the confirmation message
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<String> confirmPayment(@PathVariable String id) {
        try {
            paymentService.confirmPayment(id);
            return ResponseEntity.ok("Payment confirmed");
        } catch (Exception e) {
            logger.error("Error confirming payment", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Cancels a payment.
     *
     * @param id the payment ID
     * @return the response entity with the cancellation message
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelPayment(@PathVariable String id) {
        try {
            paymentService.cancelPayment(id);
            return ResponseEntity.ok("Payment cancelled");
        } catch (Exception e) {
            logger.error("Error cancelling payment", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
