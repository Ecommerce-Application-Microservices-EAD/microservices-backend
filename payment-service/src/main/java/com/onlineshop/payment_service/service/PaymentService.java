package com.onlineshop.payment_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlineshop.payment_service.dto.PaymentRequestDTO;
import com.onlineshop.payment_service.dto.PaymentResponseDTO;
import com.onlineshop.payment_service.exception.InvalidRequestException;
import com.onlineshop.payment_service.exception.ResourceNotFoundException;
import com.onlineshop.payment_service.model.Payment;
import com.onlineshop.payment_service.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentResponseDTO initiatePayment(PaymentRequestDTO paymentRequestDto) {

        if (paymentRequestDto == null) {
            throw new InvalidRequestException("Payment request is required.");
        }

        if (paymentRequestDto.getAmount() == null) {
            throw new InvalidRequestException("Payment amount is required.");
        }

        if (paymentRequestDto.getAmount() <= 0) {
            throw new InvalidRequestException("Payment amount must be greater than zero.");
        }

     /*    Payment payment = new Payment();
        payment.setAmount(paymentRequestDto.getAmount());
        payment.setStatus("INITIATED");
        payment.setTimestamp(LocalDateTime.now()); */

        Payment payment = new Payment(paymentRequestDto.getAmount(), "INITIATED", LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        return mapToResponseDto(savedPayment);
    }



    public PaymentResponseDTO confirmPayment(Long paymentId) {
        System.out.println("Post - /api/payments/confirm/{id} - service called");
        
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) {
            throw new ResourceNotFoundException("Payment not found.");
        }

        if (payment != null && "INITIATED".equals(payment.getStatus())) {
            payment.setStatus("CONFIRMED");
            paymentRepository.save(payment);
        }

        return mapToResponseDto(payment);
    }




    public PaymentResponseDTO cancelPayment(Long paymentId) {
        System.out.println("Post - /api/payments/cancel/{id} - service called");

        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) {
            throw new ResourceNotFoundException("Payment not found.");
        }

        if (payment != null && "INITIATED".equals(payment.getStatus())) {
            payment.setStatus("CANCELED");
            paymentRepository.save(payment);
        }
        
        return mapToResponseDto(payment);
    }


    private PaymentResponseDTO mapToResponseDto(Payment payment) {
        PaymentResponseDTO responseDto = new PaymentResponseDTO(payment.getId(), payment.getAmount(), payment.getStatus(), payment.getTimestamp());
        return responseDto;
    }


    public PaymentResponseDTO getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) {
            throw new ResourceNotFoundException("Payment not found.");
        }
        return mapToResponseDto(payment);
    }


    public List<PaymentResponseDTO> getAllPayments() {
        System.out.println("Get - /api/payments/all - service called");
        List<Payment> payments = paymentRepository.getPaymentData().values().stream().collect(Collectors.toList());

        return payments.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }
}
