// File: payment/model/Payment.java

package com.onlineshop.payment_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String status; // e.g., "INITIATED", "CONFIRMED", "CANCELED"
    private LocalDateTime timestamp;

  
     // Constructor
    public Payment(Double amount, String status, LocalDateTime timestamp) {
        this.amount = amount;
        this.status = status;
        this.timestamp = timestamp;
    } 

    // Getters and Setters
}
