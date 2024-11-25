// src/main/java/com//paymentservice/controller/PaymentController.java

package com.onlineshop.payment_service.controller;

import com.onlineshop.payment_service.model.Payment;
import com.onlineshop.payment_service.service.PaymentService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /*
     * @PostMapping("/create")
     * public ResponseEntity<String> createPayment(@RequestBody Map<String, Object>
     * paymentData) {
     * try {
     * Long amount = Long.valueOf(paymentData.get("amount").toString());
     * String currency = paymentData.get("currency").toString();
     * String clientSecret = paymentService.createPayment(amount, currency);
     * return ResponseEntity.ok(clientSecret);
     * } catch (Exception e) {
     * return ResponseEntity.status(500).body(e.getMessage());
     * }
     * }
     */

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(@RequestBody Map<String, Object> paymentData) {
        try {
            // System.out.println('1');

            Long amount = Long.valueOf(paymentData.get("amount").toString());
            String currency = paymentData.get("currency").toString();
            String userId = paymentData.get("userId").toString();

            // System.out.println("amount: " + amount);
            // System.out.println("currency: " + currency);
            // System.out.println("userId: " + userId);

            String res = paymentService.createPayment(amount, currency, userId);

        //    System.out.println("res: " + res);
            String[] parts = res.split(", ");
            String clientSecret = parts[1];
            String paymentId = parts[0];

            // System.out.println("clientSecret: " + clientSecret);
            // System.out.println("paymentId: " + paymentId);

            return ResponseEntity.ok(Map.of("clientSecret", clientSecret, "paymentId", paymentId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable String id) {
        Payment payment = paymentService.getPayment(id);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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

    @PostMapping("/{id}/confirm")
    public ResponseEntity<String> confirmPayment(@PathVariable String id) {
        try {
            paymentService.confirmPayment(id);
            return ResponseEntity.ok("Payment confirmed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelPayment(@PathVariable String id) {
        try {
            paymentService.cancelPayment(id);
            return ResponseEntity.ok("Payment cancelled");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
