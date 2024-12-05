package com.onlineshop.payment_service;

import com.onlineshop.payment_service.controller.PaymentController;
import com.onlineshop.payment_service.model.Payment;
import com.onlineshop.payment_service.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void testCreatePayment() throws Exception {
        Mockito.when(paymentService.createPayment(anyLong(), anyString(), anyString()))
                .thenReturn("12345, secretKey");

        var result = mockMvc.perform(post("/api/payments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 1000, \"currency\": \"USD\", \"userId\": \"user123\"}"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"paymentId\":\"12345\",\"clientSecret\":\"secretKey\"}", result.getResponse().getContentAsString());
    }

    @Test
    void testGetPayment() throws Exception {
        Payment payment = new Payment("USD", 1000L, "pending", "stripe123", "user123");
        payment.setId("payment123");
        Mockito.when(paymentService.getPayment("payment123")).thenReturn(payment);

        var result = mockMvc.perform(get("/api/payments/payment123"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"id\":\"payment123\",\"currency\":\"USD\",\"amount\":1000,\"status\":\"pending\",\"stripePaymentId\":\"stripe123\",\"userId\":\"user123\",\"createdDate\":null,\"lastModifiedDate\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    void testUpdatePaymentStatus() throws Exception {
        Payment updatedPayment = new Payment("USD", 1000L, "confirmed", "stripe123", "user123");
        updatedPayment.setId("payment123");
        Mockito.when(paymentService.updatePaymentStatus("payment123", "confirmed")).thenReturn(updatedPayment);

        var result = mockMvc.perform(put("/api/payments/payment123/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"confirmed\"}"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"id\":\"payment123\",\"currency\":\"USD\",\"amount\":1000,\"status\":\"confirmed\",\"stripePaymentId\":\"stripe123\",\"userId\":\"user123\",\"createdDate\":null,\"lastModifiedDate\":null}",
                result.getResponse().getContentAsString());
    }


    @Test
    void testConfirmPayment() throws Exception {
        Mockito.doNothing().when(paymentService).confirmPayment("payment123");

        var result = mockMvc.perform(post("/api/payments/payment123/confirm"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Payment confirmed", result.getResponse().getContentAsString());
    }

    @Test
    void testCancelPayment() throws Exception {
        Mockito.doNothing().when(paymentService).cancelPayment("payment123");

        var result = mockMvc.perform(post("/api/payments/payment123/cancel"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Payment cancelled", result.getResponse().getContentAsString());
    }
}
