package com.onlineshop.product_service.dto;

import java.math.BigDecimal;

public record ProductRequest(
        String name,
        String description,
        BigDecimal price,
        int stock,
        String category,
        byte[] imageData
) {
}
