package com.onlineshop.payment_service.model;

import lombok.Data;

@Data
public class Item {
    private String productId;
    private String name;
    private int quantity;
    private double price;
    private String userId;
}
