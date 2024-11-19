package com.onlineshop.payment_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    private String userId;
    private List<Item> items;
}
