package com.onlineshop.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Product name cannot be empty")
        String name,

        @NotBlank(message = "Product description cannot be empty")
        String description,

        @Min(value = 0, message = "Product price must be greater than or equal to 0")
        BigDecimal price
) {}
