// src/main/java/com/example/paymentservice/model/Payment.java

package com.onlineshop.payment_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Represents a payment in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    @NotNull(message = "Currency cannot be null")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter code")
    private String currency;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Long amount;

    @NotNull(message = "Status cannot be null")
    private String status;

    @NotNull(message = "Stripe Payment ID cannot be null")
    private String stripePaymentId;

    @NotNull(message = "User ID cannot be null")
    private String userId;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    /**
     * Constructs a new Payment with the specified details.
     *
     * @param currency the currency of the payment
     * @param amount the amount of the payment
     * @param status the status of the payment
     * @param stripePaymentId the Stripe payment ID
     * @param userId the user ID
     */
    public Payment(String currency, Long amount, String status, String stripePaymentId, String userId) {
        this.currency = currency;
        this.amount = amount;
        this.status = status;
        this.stripePaymentId = stripePaymentId;
        this.userId = userId;
    }
}
