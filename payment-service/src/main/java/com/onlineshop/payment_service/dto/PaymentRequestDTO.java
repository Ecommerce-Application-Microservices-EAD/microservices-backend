package com.onlineshop.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentRequestDTO {
    
    private Double amount;
    

    @Override
    public String toString() {
        return "PaymentRequestDTO {" +
                "amount=" + amount + 
                "}";
    }

}