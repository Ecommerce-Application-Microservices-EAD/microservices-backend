// src/main/java/com//paymentservice/service/PaymentService.java

package com.onlineshop.payment_service.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlineshop.payment_service.exception.ResourceNotFoundException;
import com.onlineshop.payment_service.model.Payment;
import com.onlineshop.payment_service.repository.PaymentRepository;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_CONFIRMED = "confirmed";

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Creates a payment.
     *
     * @param amount   the amount of the payment
     * @param currency the currency of the payment
     * @param userId   the user ID
     * @return the payment ID and client secret
     */
    public String createPayment(Long amount, String currency, String userId) {
        logger.info("Creating payment: amount={}, currency={}, userId={}", amount, currency, userId);

        if (amount <= 0 || currency == null || currency.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Invalid payment details");
        }

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            Payment payment = new Payment(currency, amount, STATUS_PENDING, paymentIntent.getId(), userId);
            paymentRepository.save(payment);

            return payment.getId() + ", " + paymentIntent.getClientSecret();
        } catch (Exception e) {
            logger.error("Error creating payment", e);
            throw new RuntimeException("Payment creation failed", e);
        }
    }

    /**
     * Retrieves a payment by ID.
     *
     * @param paymentId the payment ID
     * @return the payment
     */
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> 
            new ResourceNotFoundException("Payment not found: " + paymentId));
    }

    /**
     * Updates the status of a payment.
     *
     * @param paymentId the payment ID
     * @param status    the new status
     * @return the updated payment
     */
    public Payment updatePaymentStatus(String paymentId, String status) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(status);
            paymentRepository.save(payment);
            return payment;
        } else {
            throw new ResourceNotFoundException("Payment not found: " + paymentId);
        }
    }

    /**
     * Confirms a payment.
     *
     * @param paymentId the payment ID
     * @throws Exception if an error occurs
     */
    public void confirmPayment(String paymentId) throws Exception {
        logger.info("Confirming payment: paymentId={}", paymentId);
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(STATUS_CONFIRMED);
            paymentRepository.save(payment);
        } else {
            throw new ResourceNotFoundException("Payment not found: " + paymentId);
        }
    }

    /**
     * Cancels a payment.
     *
     * @param paymentId the payment ID
     * @throws Exception if an error occurs
     */
    public void cancelPayment(String paymentId) throws Exception {
        logger.info("Cancelling payment: paymentId={}", paymentId);
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus("cancelled");
            paymentRepository.save(payment);
        } else {
            throw new ResourceNotFoundException("Payment not found: " + paymentId);
        }
    }
}
