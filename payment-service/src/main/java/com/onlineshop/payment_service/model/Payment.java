// src/main/java/com/example/paymentservice/model/Payment.java

package com.onlineshop.payment_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "payments")
public class Payment {
    
    @Id
    private String id;
    private String currency;
    private Long amount;
    private String status;
    private String stripePaymentId;

    // constructors
    public Payment(String currency, Long amount, String status, String stripePaymentId) {
        this.currency = currency;
        this.amount = amount;
        this.status = status;
        this.stripePaymentId = stripePaymentId;
    }
}
