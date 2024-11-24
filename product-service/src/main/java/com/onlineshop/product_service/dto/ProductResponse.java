package com.onlineshop.product_service.dto;

import jdk.jshell.Snippet;
import lombok.Builder;

import java.math.BigDecimal;

public record ProductResponse(String id, String name, String description, BigDecimal price, String category) {
    @Builder
    public ProductResponse {
    }
}

