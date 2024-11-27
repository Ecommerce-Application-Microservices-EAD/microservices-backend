package com.onlineshop.payment_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;



/**
 * Represents an item in the cart.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @NotNull(message = "Product ID cannot be null")
    private String productId;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @Positive(message = "Quantity must be positive")
    private int quantity;

    @Positive(message = "Price must be positive")
    private double price;

    @NotNull(message = "User ID cannot be null")
    private String userId;
}
