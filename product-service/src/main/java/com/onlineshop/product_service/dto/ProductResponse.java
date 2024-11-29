package com.onlineshop.product_service.dto;

import lombok.Builder;

import java.math.BigDecimal;

public record ProductResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        String category,
        byte[] imageData
) {
    @Builder
    public ProductResponse {
    }
}
