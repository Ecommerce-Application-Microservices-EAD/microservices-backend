package com.onlineshop.payment_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentResponseDTO {
    private Long id;
    private Double amount;
    private String status;
    private LocalDateTime timestamp;


    @Override
    public String toString() {
        return "PaymentResponseDTO {" +
                "id=" + id + 
                ", amount=" + amount + 
                ", status=" + status + 
                ", timestamp=" + timestamp + 
                "}";
    }

}