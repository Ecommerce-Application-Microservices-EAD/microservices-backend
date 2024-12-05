package com.onlineshop.payment_service.dto;

// OrderDTO.java
import java.util.List;

import com.onlineshop.payment_service.model.Item;

import lombok.Data;

@Data
public class OrderDTO {
    private String userId;
    private List<Item> items;
    private String status;
    private String shippingAddress;
    private Double totalAmount;
}
