package com.onlineshop.order_service.service;

import com.onlineshop.order_service.client.InventoryClient;
import com.onlineshop.order_service.dto.OrderRequest;
import com.onlineshop.order_service.dto.OrderResponse;
import com.onlineshop.order_service.exception.OrderNotFoundException;
import com.onlineshop.order_service.model.Order;
import com.onlineshop.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (isProductInStock) {
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .skuCode(orderRequest.skuCode())
                    .price(orderRequest.price())
                    .quantity(orderRequest.quantity())
                    .build();
            orderRepository.save(order);
            log.info("Order placed successfully");
            return OrderResponse.builder()
                    .id(order.getId())
                    .orderNumber(order.getOrderNumber())
                    .skuCode(order.getSkuCode())
                    .price(order.getPrice())
                    .quantity(order.getQuantity())
                    .build();
        }
        log.info("Order failed due to product not in stock");
throw new RuntimeException("Product "+orderRequest.skuCode()+" not in stock");
    }

    public List<OrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();
        log.info("Fetching all orders");
        return orders.stream()
                .map(order -> OrderResponse.builder()
                        .id(order.getId())
                        .orderNumber(order.getOrderNumber())
                        .skuCode(order.getSkuCode())
                        .price(order.getPrice())
                        .quantity(order.getQuantity())
                        .build())
                .toList();

    }

    public OrderResponse getOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order with Id + " + orderId + "Not Found"));
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .skuCode(order.getSkuCode())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .userId(order.getUserId())
                .build();
    }

    public List<OrderResponse> getOrderByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()){
            throw  new OrderNotFoundException("No Orders found for user with Id " + userId);
        }
        return orders.stream()
                .map(order -> OrderResponse.builder()
                        .id(order.getId())
                        .orderNumber(order.getOrderNumber())
                        .skuCode(order.getSkuCode())
                        .price(order.getPrice())
                        .quantity(order.getQuantity())
                        .userId(order.getUserId())
                        .build())
                .toList();
    }
}
