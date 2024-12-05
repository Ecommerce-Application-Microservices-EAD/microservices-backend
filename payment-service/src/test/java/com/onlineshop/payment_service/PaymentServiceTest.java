package com.onlineshop.payment_service;

import com.onlineshop.payment_service.model.Payment;
import com.onlineshop.payment_service.repository.PaymentRepository;
import com.onlineshop.payment_service.service.PaymentService;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.stripe.Stripe;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment() {
        String userId = "user123";
        Long amount = 1000L;
        String currency = "USD";
        String stripePaymentId = "stripe123";
        String clientSecret = "secret123";
        String paymentId = "payment123";

        PaymentIntent paymentIntent = mock(PaymentIntent.class);
        when(paymentIntent.getId()).thenReturn(stripePaymentId);
        when(paymentIntent.getClientSecret()).thenReturn(clientSecret);

        try {
            mockStatic(PaymentIntent.class);
            when(PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(paymentIntent);
        } catch (Exception e) {
            fail("Exception during Stripe API mocking: " + e.getMessage());
        }

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment savedPayment = invocation.getArgument(0);
            savedPayment.setId(paymentId); // Simulate database ID assignment
            return savedPayment;
        });

        String result = paymentService.createPayment(amount, currency, userId);

        String expectedResponse = paymentId + ", " + clientSecret;
        assertEquals(expectedResponse, result, "The returned payment ID and client secret should match the expected values.");

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetPayment() {

        String paymentId = "payment123";
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setAmount(1000L);
        payment.setCurrency("USD");
        payment.setStripePaymentId("stripe123");

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        Payment result = paymentService.getPayment(paymentId);

        assertNotNull(result, "Payment should not be null");
        assertEquals(paymentId, result.getId(), "Payment ID should match");
        assertEquals(1000L, result.getAmount(), "Payment amount should match");
        assertEquals("USD", result.getCurrency(), "Payment currency should match");
        verify(paymentRepository, times(1)).findById(paymentId);
    }

    @Test
    void testUpdatePaymentStatus() {

        String paymentId = "payment123";
        String newStatus = "COMPLETED";
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus("PENDING");

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment updatedPayment = paymentService.updatePaymentStatus(paymentId, newStatus);

        assertNotNull(updatedPayment, "Updated payment should not be null");
        assertEquals(newStatus, updatedPayment.getStatus(), "Payment status should be updated");
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testConfirmPayment() throws Exception {
        Payment payment = new Payment("USD", 1000L, "pending", "stripe123", "user123");
        payment.setId("payment123");
        when(paymentRepository.findById("payment123")).thenReturn(Optional.of(payment));

        paymentService.confirmPayment("payment123");

        assertEquals("confirmed", payment.getStatus(), "Payment status should be 'CONFIRMED'");

        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testCancelPayment() throws Exception {
        Payment payment = new Payment("USD", 1000L, "pending", "stripe123", "user123");
        payment.setId("payment123");
        when(paymentRepository.findById("payment123")).thenReturn(Optional.of(payment));

        paymentService.cancelPayment("payment123");

        assertEquals("cancelled", payment.getStatus(), "Payment status should be 'CANCELLED'");

        verify(paymentRepository, times(1)).save(payment);
    }


}
