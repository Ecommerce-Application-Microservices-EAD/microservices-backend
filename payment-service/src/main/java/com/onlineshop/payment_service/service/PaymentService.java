// src/main/java/com//paymentservice/service/PaymentService.java

package com.onlineshop.payment_service.service;

import com.onlineshop.payment_service.model.Payment;
import com.onlineshop.payment_service.repository.PaymentRepository;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public String createPayment(Long amount, String currency) {
        System.out.println("createPayment: " + amount + ", " + currency);
        // Create a PaymentIntent with Stripe
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .build();

        // System.out.println("params: " + params);

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            // System.out.println("paymentIntent: " + paymentIntent);

            // Save payment details to MongoDB
            Payment payment = new Payment(currency, amount, "pending", paymentIntent.getId());
            paymentRepository.save(payment);

            return payment.getId() + ", " + paymentIntent.getClientSecret();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId).orElse(null);
    }

    public Payment updatePaymentStatus(String paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment != null) {
            payment.setStatus(status);
            paymentRepository.save(payment);
        }
        return payment;
    }

    public void confirmPayment(String paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment != null) {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(payment.getStripePaymentId());
            paymentIntent.confirm();
            payment.setStatus("confirmed");
            paymentRepository.save(payment);
        }
    }

    public void cancelPayment(String paymentId) throws Exception {

    }
}
