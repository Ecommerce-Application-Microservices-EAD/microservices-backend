package com.onlineshop.payment_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshop.payment_service.dto.PaymentRequestDTO;
import com.onlineshop.payment_service.dto.PaymentResponseDTO;
import com.onlineshop.payment_service.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

//    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @GetMapping()
    public String Hello() {
        System.out.println("Get - /api/payments - controller called");
//        log.info("Get - /api/payments - controller called");
        return "Hello World";
    }

    @GetMapping("/{id}")
    public PaymentResponseDTO getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/all")
    public List<PaymentResponseDTO> getAllPayments() {
        System.out.println("Get - /api/payments/all - controller called");
        return paymentService.getAllPayments();
    }

    @PostMapping("/initiate")
    public PaymentResponseDTO initiatePayment(@RequestBody PaymentRequestDTO paymentRequestDto) {
        System.out.println("Post - /api/payments/initiate - controller called");
        return paymentService.initiatePayment(paymentRequestDto);
    }

    @PostMapping("/confirm/{id}")
    public PaymentResponseDTO confirmPayment(@PathVariable Long id) {
        System.out.println("Post - /api/payments/confirm/{id} - controller called");
        return paymentService.confirmPayment(id);
    }

    @PostMapping("/cancel/{id}")
    public PaymentResponseDTO cancelPayment(@PathVariable Long id) {
        System.out.println("Post - /api/payments/cancel/{id} - controller called");
        return paymentService.cancelPayment(id);
    }
}
