package com.onlineshop.order_service.dto;

import lombok.Builder;

import java.math.BigDecimal;

public record OrderResponse(Long id, String orderNumber, String skuCode, BigDecimal price, Integer quantity){

    @Builder
    public OrderResponse {
    }
}
